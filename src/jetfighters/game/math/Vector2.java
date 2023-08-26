package jetfighters.game.math;

/**
 * /** <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Represents the mathematical construct of a vector in 2-dimensional space.
 * Vectors are final and method results always return a new vector with the appropriate values.
 * Used for game-related math.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
@SuppressWarnings("ClassCanBeRecord")
public class Vector2 {

    // Commonly used vectors
    public static final Vector2 ZERO = new Vector2(0, 0);
    public static final Vector2 UP = new Vector2(0, -1);
    public static final Vector2 DOWN = new Vector2(0, 1);
    public static final Vector2 LEFT = new Vector2(-1, 0);
    public static final Vector2 RIGHT = new Vector2(1, 0);
    public static final Vector2 ONE = new Vector2(1, 1);

    /**
     * Creates a new vector in cartesian coordinates from a magnitude and angle
     *
     * @param magnitude magnitude (length) of the new vector
     * @param angle     angle of the new vector (relative to {@link #RIGHT} - clockwise)
     * @return the newly constructed vector with {@link #length()} = {@code magnitude} and {@link #angle()} = {@code angle}
     */
    public static Vector2 toCartesian(double magnitude, double angle) {
        return new Vector2(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }


    // Vector 2 components (final as permutation is not intended, only new Vector2's are returned in calculations)
    // These are public to simplify their usage. As they are final they cannot be changed a setter isn't needed
    // and Vector2.getX() is simplified to Vector2.x
    public final double x, y;

    /**
     * Creates a new Vector2
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds two scalar values to this vector's components and returns the result as a new vector.
     *
     * @param x value to add to x-component
     * @param y value to add to y-component
     * @return a new vector with the calculated values
     */
    public Vector2 add(double x, double y) {
        return new Vector2(this.x + x, this.y + y);
    }

    /**
     * Adds the values of {@code vec} to this vector component-wise.
     *
     * @param vec the vector to add
     * @return a new vector with the calculated values
     */
    public Vector2 add(Vector2 vec) {
        return new Vector2(this.x + vec.x, this.y + vec.y);
    }

    /**
     * Subtracts two scalar values from this vector's components and returns the result as a new vector.
     *
     * @param x value to subtract from x-component
     * @param y value to subtract from y-component
     * @return a new vector with the calculated values
     */
    public Vector2 sub(double x, double y) {
        return new Vector2(this.x - x, this.y - y);
    }

    /**
     * Subtracts the values of {@code vec} from this vector component-wise.
     *
     * @param vec the vector to subtract from this
     * @return a new vector with the calculated values
     */
    public Vector2 sub(Vector2 vec) {
        return new Vector2(this.x - vec.x, this.y - vec.y);
    }

    /**
     * Multiplies this vector's components by the same {@code scalar} and returns the result as a new vector.
     *
     * @param scalar the scalar to multiply both components with
     * @return a new vector with the calculated values
     */
    public Vector2 mult(double scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    /**
     * Multiplies two scalar values with this vector's components and returns the result as a new vector.
     *
     * @param scalarX value to multiply with x-component
     * @param scalarY value to multiply with y-component
     * @return a new vector with the calculated values
     */
    public Vector2 mult(double scalarX, double scalarY) {
        return new Vector2(x * scalarX, y * scalarY);
    }

    /**
     * Multiplies the values of {@code scalarVec} with this vector component-wise (x * scalarVec.x, y * scalarVec.y).
     *
     * @param scalarVec the vector to multiply with this
     * @return a new vector with the calculated values
     */
    public Vector2 mult(Vector2 scalarVec) {
        return new Vector2(x * scalarVec.x, y * scalarVec.y);
    }

    /**
     * Divides this vector's components by the same {@code dividend} and returns the result as a new vector.
     *
     * @param dividend the dividend to divide both components with
     * @return a new vector with the calculated values
     */
    public Vector2 div(double dividend) {
        return new Vector2(x / dividend, y / dividend);
    }

    /**
     * Divides this vector's components by two scalar values and returns the result as a new vector.
     *
     * @param dividendX dividend to divide x-component by
     * @param dividendY dividend to divide y-component by
     * @return a new vector with the calculated values
     */
    public Vector2 div(double dividendX, double dividendY) {
        return new Vector2(x / dividendX, y / dividendY);
    }

    /**
     * Divides this vector by {@code dividendVec} component-wise (x / dividendVec.x, y / dividendVec.y).
     *
     * @param dividendVec the vector to divide this by
     * @return a new vector with the calculated values
     */
    public Vector2 div(Vector2 dividendVec) {
        return new Vector2(x / dividendVec.x, y / dividendVec.y);
    }

    /**
     * @return the dot product of this vector and {@code vec}
     */
    public double dot(Vector2 vec) {
        return x * vec.x + y * vec.y;
    }

    /**
     * @return the length (or magnitude) of this vector
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * @return the distance between this vector and the point ({@code x}, {@code y})
     */
    public double distanceTo(double x, double y) {
        return Math.sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y));
    }

    /**
     * @return the distance between this vector and {@code vec}
     */
    public double distanceTo(Vector2 vec) {
        return distanceTo(vec.x, vec.y);
    }

    /**
     * @return the angle of this vector in radians ({@link #RIGHT} has an angle of 0, {@link #DOWN} of PI/2)
     */
    public double angle() {
        return Math.atan2(y, x);
    }

    /**
     * @return the angle of this vector in relation to {@code other} in radians (other.angle() - angle())
     */
    public double angleTo(Vector2 other) {
        return other.angle() - angle();
    }

    /**
     * @return normalization of this vector (vector in the same direction with length of 1)
     */
    public Vector2 normalized() {
        return div(length());
    }

    /**
     * @return this vector in reverse (-this)
     */
    public Vector2 reversed() {
        return new Vector2(-x, -y);
    }

    /**
     * Rotates this vector by {@code angle} in clockwise direction.
     *
     * @param angle angle to rotate by in radians
     * @return a new vector with the calculated values
     */
    public Vector2 rotated(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector2(x * cos - y * sin, x * sin + y * cos);
    }

    /**
     * Rotates this vector to {@code angle} keeping its length.
     *
     * @param angle angle to rotate to in radians
     * @return a new vector with the calculated values
     */
    public Vector2 rotatedTo(double angle) {
        return toCartesian(length(), angle);
    }

    /**
     * Interpolates between the values of this vector and {@code to} linearly.
     *
     * @param to    the end point of the interpolation
     * @param delta [0,1] 0 = this vector, 1 = {@code to}, ]0,1[ = the vector that lies between this and to at {@code delta} * distance
     * @return the interpolated vector
     */
    public Vector2 lerp(Vector2 to, double delta) {
        return new Vector2(x + (to.x - x) * delta, y + (to.y - y) * delta);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2 vector2 = (Vector2) o;

        if (Double.compare(vector2.x, x) != 0) return false;
        return Double.compare(vector2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    protected Vector2 clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning a Vector2 is not necessary, as it's fields are final.");
    }

}
