package com.andyadc.codeblocks.common.type;

import com.andyadc.codeblocks.common.annotation.Nullable;

/**
 * Pair, 用于返回值返回两个元素.
 *
 * @author andaicheng
 * @since 2018/6/2
 */
public class Pair<L, R> {

    @Nullable
    private final L left;
    @Nullable
    private final R right;

    public Pair(@Nullable L left, @Nullable R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * 根据等号左边的泛型，自动构造合适的 Pair
     */
    public static <L, R> Pair<L, R> of(@Nullable L left, @Nullable R right) {
        return new Pair<>(left, right);
    }

    @Nullable
    public L getLeft() {
        return left;
    }

    @Nullable
    public R getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        return prime * result + ((right == null) ? 0 : right.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pair other = (Pair) obj;
        if (left == null) {
            if (other.left != null) {
                return false;
            }
        } else if (!left.equals(other.left)) {
            return false;
        }
        if (right == null) {
            return other.right == null;
        } else return right.equals(other.right);
    }

    @Override
    public String toString() {
        return "Pair [left=" + left + ", right=" + right + ']';
    }
}
