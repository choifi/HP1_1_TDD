package io.hhplus.tdd.point;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserPointTest {

    @Test
    void 포인트_충전_성공_시_포인트_증가한다() {
        UserPoint userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
        UserPoint plusCharged = userPoint.charge(500L);
        assertThat(plusCharged.point()).isEqualTo(1500L);
    }

    @Test
    void 포인트_충전_음수_시_예외_발생한다() {
        UserPoint userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
        assertThrows(IllegalArgumentException.class, () -> {
            userPoint.charge(-500L);
        });
    }


    @Test
    void 포인트_사용_성공_시_포인트_감소한다() {
        UserPoint userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
        UserPoint usePlusPoint = userPoint.use(500);
        assertThat(usePlusPoint.point()).isEqualTo(500L);
    }

    @Test
    void 포인트_사용_음수_시_예외_발생한다() {
        UserPoint userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
        assertThrows(IllegalArgumentException.class, () -> {
            userPoint.use(-500L);
        });
    }

}