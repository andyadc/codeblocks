package com.andyadc.codeblocks.common.type;

import com.andyadc.codeblocks.common.annotation.Nullable;

/**
 * Triple, 用于返回值返回三个元素.
 *
 * @author andaicheng
 * @since 2018/6/2
 */
public class Triple<L, M, R> {

    @Nullable
    private final L left;
    @Nullable
    private final M middle;
    @Nullable
    private final R right;

    /**
     * Creates a new Triple.
     */
    public Triple(@Nullable L left, @Nullable M middle, @Nullable R right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    /**
     * 根据等号左边的泛型，自动构造合适的Triple
     */
    public static <L, M, R> Triple<L, M, R> of(@Nullable L left, @Nullable M middle, @Nullable R right) {
        return new Triple<>(left, middle, right);
    }

    @Nullable
    public L getLeft() {
        return left;
    }

    @Nullable
    public M getMiddle() {
        return middle;
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
        result = prime * result + ((middle == null) ? 0 : middle.hashCode());
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
        Triple other = (Triple) obj;
        if (left == null) {
            if (other.left != null) {
                return false;
            }
        } else if (!left.equals(other.left)) {
            return false;
        }
        if (middle == null) {
            if (other.middle != null) {
                return false;
            }
        } else if (!middle.equals(other.middle)) {
            return false;
        }
        if (right == null) {
            return other.right == null;
        } else return right.equals(other.right);
    }

    @Override
    public String toString() {
        return "Triple [left=" + left + ", middle=" + middle + ", right=" + right + ']';
    }
}
