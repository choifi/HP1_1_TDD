package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    long userId;
    long initialPoint;

    @Mock
    private UserPointTable userPointTable;

    @Mock
    private PointHistoryTable pointHistoryTable;

    @InjectMocks
    private UserPointService userPointService;

    @BeforeEach
    void setUp() {
        userId = 1L;
        initialPoint = 1000L;
    }

    @Test
    void 초기_포인트가_1000인_사용자가_500_충전시_최종_포인트는_1500이다() {

        UserPoint current = new UserPoint(userId, initialPoint, System.currentTimeMillis());
        UserPoint updated = new UserPoint(userId, initialPoint + 500L, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(current).thenReturn(updated);
        when(userPointTable.insertOrUpdate(userId, updated.point())).thenReturn(updated);

        UserPoint result = userPointService.charge(userId, 500L);

        assertThat(result.point()).isEqualTo(1500L);

        verify(userPointTable).selectById(userId);
        verify(userPointTable).insertOrUpdate(userId, 1500L);
        verify(pointHistoryTable).insert(eq(userId), eq(500L), eq(TransactionType.CHARGE), anyLong());
    }

    @Test
    void 초기_포인트가_1000인_사용자가_500_사용시_최종_포인트는_500이다() {
        UserPoint current = new UserPoint(userId, initialPoint, System.currentTimeMillis());
        UserPoint updated = new UserPoint(userId, initialPoint - 500L, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(current).thenReturn(updated);
        when(userPointTable.insertOrUpdate(userId, updated.point())).thenReturn(updated);

        UserPoint result = userPointService.use(userId, 500L);

        assertThat(result.point()).isEqualTo(500L);

        verify(userPointTable).selectById(userId);
        verify(userPointTable).insertOrUpdate(userId, 500L);
        verify(pointHistoryTable).insert(eq(userId), eq(500L), eq(TransactionType.USE), anyLong());
    }

    @Test
    void 사용자가_1000_가지고_있을_때_1500을_사용하면_예외_발생한다() {
        UserPoint current = new UserPoint(userId, initialPoint, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(current);

        assertThatThrownBy(() -> userPointService.use(userId, 1500L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잔여 포인트가 부족합니다.");

        verify(userPointTable).selectById(userId);
        verifyNoMoreInteractions(userPointTable, pointHistoryTable);
    }

}