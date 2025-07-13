package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@SpringBootTest
class PointIntegrationTest {
    @Autowired
    UserPointService userPointService;

    @Autowired
    private UserPointTable userPointTable;

    @Autowired
    private PointHistoryTable pointHistoryTable;

    @Test
    void 초기_포인트가_0인_사용자가_500_충전_시_최종_포인트는_500이다() {

        // when
        UserPoint charged = userPointService.charge(1L,500L);

        // then
        assertThat(charged.point()).isEqualTo(500L);
    }

    @Test
    void 초기_포인트가_1000인_사용자가_300_사용_시_포인트는_700이다() {
        // given
        userPointService.charge(2L, 1000L);

        // when
        UserPoint used = userPointService.use(2L, 300L);

        // then
        assertThat(used.point()).isEqualTo(700L);
    }

    @Test
    void 사용자의_포인트_1000_충전과_300_사용시_조회하면_700이다() {
        // given
        userPointService.charge(3L, 1000L);
        userPointService.use(3L, 300L);

        // when
        UserPoint user = userPointService.getUserPoint(3L);

        // then
        assertThat(user.point()).isEqualTo(700L);
    }

    @Test
    void 사용자의_포인트를_1000_충전하고_300_사용시_히스토리_사이즈는_2다() {
        // given
        userPointService.charge(4L, 1000L);
        userPointService.use(4L, 300L);

        // when
        List<PointHistory> histories = userPointService.getUserHistory(4L);

        // then
        assertThat(histories).hasSize(2);
    }



}