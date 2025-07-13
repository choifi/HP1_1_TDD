package io.hhplus.tdd.point;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public UserPoint charge(long amount) {

       if (amount < 0) {
           throw new IllegalArgumentException("포인트 충전 금액은 0 이상이여야 합니다.");
       }
       return new UserPoint(id, point + amount, System.currentTimeMillis());
    }

    public UserPoint use(long amount) {

        if (amount < 0) {
            throw new IllegalArgumentException("포인트 사용 금액은 0 이상이여야 합니다.");
        }
        if (point < amount) {
            throw new IllegalArgumentException("포인트 잔액이 부족합니다.");
        }

        return new UserPoint(id, point - amount, System.currentTimeMillis());
    }

}
