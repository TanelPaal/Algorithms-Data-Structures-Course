import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Quaternions. Basic operations. */
public class Quaternion {

   private double a, b, c, d;
   private static final double THRESHOLD = 0.000001;

   /** Constructor from four double values.
    * @param a real part
    * @param b imaginary part i
    * @param c imaginary part j
    * @param d imaginary part k
    */
   public Quaternion (double a, double b, double c, double d) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.d = d;
   }

   /** Real part of the quaternion.
    * @return real part
    */
   public double getRpart() {
      return a;
   }

   /** Imaginary part i of the quaternion. 
    * @return imaginary part i
    */
   public double getIpart() {
      return b;
   }

   /** Imaginary part j of the quaternion. 
    * @return imaginary part j
    */
   public double getJpart() {
      return c;
   }

   /** Imaginary part k of the quaternion. 
    * @return imaginary part k
    */
   public double getKpart() {
      return d;
   }

   /** Conversion of the quaternion to the string.
    * @return a string form of this quaternion: 
    * "a+bi+cj+dk"
    * (without any brackets)
    */
   @Override
   public String toString() {
      return String.format("%f%+fi%+fj%+fk", a, b, c, d)
              .replace("+-", "-")
              .replace("-+", "-");
   }

   /** Conversion from the string to the quaternion. 
    * Reverse to <code>toString</code> method.
    * @throws IllegalArgumentException if string s does not represent 
    *     a quaternion (defined by the <code>toString</code> method)
    * @param s string of form produced by the <code>toString</code> method
    * @return a quaternion represented by string s
    */
   public static Quaternion valueOf (String s) {
      double a = 0.0, b = 0.0, c = 0.0, d = 0.0;

      String regex = "([+-]?\\d+(?:\\.\\d+)?(?:[eE][+-]?\\d+)?)([ijk])?";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(s);

      while (matcher.find()) {
         String numericValue = matcher.group(1);
         String component = matcher.group(2);

         double value;
         try {
            value = Double.parseDouble(numericValue);
         } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in quaternion string: " + numericValue + " from " + s, e);
         }

         if (component != null) {
            switch (component.charAt(0)) {
               case 'i':
                  b = value;
                  break;
               case 'j':
                  c = value;
                  break;
               case 'k':
                  d = value;
                  break;
               default:
                  throw new IllegalArgumentException("Invalid imaginary part in quaternion string: " + component + " from " + s);
            }
         } else {
            a = value;
         }
      }
      return new Quaternion(a, b, c, d);
   }

   /** Clone of the quaternion.
    * @return independent clone of <code>this</code>
    */
   @Override
   public Object clone() throws CloneNotSupportedException {
      return new Quaternion(a, b, c, d);
   }

   /** Test whether the quaternion is zero. 
    * @return true, if the real part and all the imaginary parts are (close to) zero
    */
   public boolean isZero() {
      return Math.abs(a) < THRESHOLD && Math.abs(b) < THRESHOLD &&
              Math.abs(c) < THRESHOLD && Math.abs(d) < THRESHOLD;
   }

   /** Conjugate of the quaternion. Expressed by the formula 
    *     conjugate(a+bi+cj+dk) = a-bi-cj-dk
    * @return conjugate of <code>this</code>
    */
   public Quaternion conjugate() {
      return new Quaternion(a, -b, -c, -d);
   }

   /** Opposite of the quaternion. Expressed by the formula 
    *    opposite(a+bi+cj+dk) = -a-bi-cj-dk
    * @return quaternion <code>-this</code>
    */
   public Quaternion opposite() {
      return new Quaternion(-a, -b, -c, -d);
   }

   /** Sum of quaternions. Expressed by the formula 
    *    (a1+b1i+c1j+d1k) + (a2+b2i+c2j+d2k) = (a1+a2) + (b1+b2)i + (c1+c2)j + (d1+d2)k
    * @param q addend
    * @return quaternion <code>this+q</code>
    */
   public Quaternion plus (Quaternion q) {
      return new Quaternion(a + q.a, b + q.b, c + q.c, d + q.d);
   }

   /** Product of quaternions. Expressed by the formula
    *  (a1+b1i+c1j+d1k) * (a2+b2i+c2j+d2k) = (a1a2-b1b2-c1c2-d1d2) + (a1b2+b1a2+c1d2-d1c2)i +
    *  (a1c2-b1d2+c1a2+d1b2)j + (a1d2+b1c2-c1b2+d1a2)k
    * @param q factor
    * @return quaternion <code>this*q</code>
    */
   public Quaternion times (Quaternion q) {
      double newA = a * q.a - b * q.b - c * q.c - d * q.d;
      double newB = a * q.b + b * q.a + c * q.d - d * q.c;
      double newC = a * q.c - b * q.d + c * q.a + d * q.b;
      double newD = a * q.d + b * q.c - c * q.b + d * q.a;
      return new Quaternion(newA, newB, newC, newD);
   }

   /** Multiplication by a coefficient.
    * @param r coefficient
    * @return quaternion <code>this*r</code>
    */
   public Quaternion times (double r) {
      return new Quaternion(a * r, b * r, c * r, d * r);
   }

   /** Inverse of the quaternion. Expressed by the formula
    *     1/(a+bi+cj+dk) = a/(a*a+b*b+c*c+d*d) + 
    *     ((-b)/(a*a+b*b+c*c+d*d))i + ((-c)/(a*a+b*b+c*c+d*d))j + ((-d)/(a*a+b*b+c*c+d*d))k
    * @return quaternion <code>1/this</code>
    */
   public Quaternion inverse() {
      double normSquared = a * a + b * b + c * c + d * d;
      if (normSquared < THRESHOLD) {
         throw new ArithmeticException("Division by zero: norm is too small");
      }
      return new Quaternion(a / normSquared, -b / normSquared, -c / normSquared, -d / normSquared);
   }

   /** Difference of quaternions. Expressed as addition to the opposite.
    * @param q subtrahend
    * @return quaternion <code>this-q</code>
    */
   public Quaternion minus (Quaternion q) {
      return new Quaternion(a - q.a, b - q.b, c - q.c, d - q.d);
   }

   /** Right quotient of quaternions. Expressed as multiplication to the inverse.
    * @param q (right) divisor
    * @return quaternion <code>this*inverse(q)</code>
    */
   public Quaternion divideByRight (Quaternion q) {
      if (q.isZero()) {
         throw new ArithmeticException("Division by zero");
      }
      return this.times(q.inverse());
   }

   /** Left quotient of quaternions.
    * @param q (left) divisor
    * @return quaternion <code>inverse(q)*this</code>
    */
   public Quaternion divideByLeft (Quaternion q) {
      if (q.isZero()) {
         throw new ArithmeticException("Division by zero");
      }
      return q.inverse().times(this);
   }
   
   /** Equality test of quaternions. Difference of equal numbers
    *     is (close to) zero.
    * @param qo second quaternion
    * @return logical value of the expression <code>this.equals(qo)</code>
    */
   @Override
   public boolean equals (Object qo) {
      if (this == qo) return true;
      if (qo == null || getClass() != qo.getClass()) return false;
      Quaternion that = (Quaternion) qo;
      return Math.abs(a - that.a) < THRESHOLD &&
              Math.abs(b - that.b) < THRESHOLD &&
              Math.abs(c - that.c) < THRESHOLD &&
              Math.abs(d - that.d) < THRESHOLD;
   }

   /** Dot product of quaternions. (p*conjugate(q) + q*conjugate(p))/2
    * @param q factor
    * @return dot product of this and q
    */
   public Quaternion dotMult (Quaternion q) {
      Quaternion pConjugate = this.conjugate();
      Quaternion qConjugate = q.conjugate();
      Quaternion product1 = this.times(qConjugate);
      Quaternion product2 = q.times(pConjugate);
      return product1.plus(product2).times(0.5);
   }

   /** Integer hashCode has to be the same for equal objects.
    * @return hashcode
    */
   @Override
   public int hashCode() {
      return Objects.hash(a, b, c, d);
   }

   /** Norm of the quaternion. Expressed by the formula 
    *     norm(a+bi+cj+dk) = Math.sqrt(a*a+b*b+c*c+d*d)
    * @return norm of <code>this</code> (norm is a real number)
    */
   public double norm() {
      return Math.sqrt(a * a + b * b + c * c + d * d);
   }

   public Quaternion pow(int n) {
      if (n == 0) {
         return new Quaternion(1, 0, 0, 0);
      } else if (n == 1) {
         return new Quaternion(a, b, c, d);
      } else if (n == -1) {
         if (this.isZero()) {
            throw new ArithmeticException("Division by zero");
         }
         return this.inverse();
      } else if (n > 1) {
         return this.times(this.pow(n - 1));
      } else { // n < -1
         return this.pow(-n).inverse();
      }
   }

   /** Main method for testing purposes. 
    * @param arg command line parameters
    */
   public static void main (String[] args) {
      // TODO!!! Your example runs here

      // Create two quaternions
      Quaternion q1 = new Quaternion(1.0, 2.0, 3.0, 4.0);
      Quaternion q2 = new Quaternion(0.5, -1.0, 1.5, -2.0);

      // Print the quaternions
      System.out.println("q1: " + q1);
      System.out.println("q2: " + q2);

      // Add the quaternions
      Quaternion sum = q1.plus(q2);
      System.out.println("q1 + q2: " + sum);

      // Multiply the quaternions
      Quaternion product = q1.times(q2);
      System.out.println("q1 * q2: " + product);

      // Conjugate of q1
      Quaternion conjugate = q1.conjugate();
      System.out.println("Conjugate of q1: " + conjugate);

      // Inverse of q1
      Quaternion inverse = q1.inverse();
      System.out.println("Inverse of q1: " + inverse);

      // Norm of q1
      double norm = q1.norm();
      System.out.println("Norm of q1: " + norm);

      // Check if q1 is zero
      boolean isZero = q1.isZero();
      System.out.println("Is q1 zero? " + isZero);

      // Convert q1 to string and back
      String q1String = q1.toString();
      Quaternion q1FromString = Quaternion.valueOf(q1String);
      System.out.println("q1 from string: " + q1FromString);
   }

}

