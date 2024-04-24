package Physics;

public class Matrix {

   private float[][] matrix;

   public Matrix (Vector base_1, Vector base_2) {
       matrix = new float[2][2];
       matrix[0][0] = base_1.x;
       matrix[0][1] = base_2.x;
       matrix[1][0] = base_1.y;
       matrix[1][1] = base_2.y;
   }


   public String toString () {
       return '\n' + matrix[0][0] + " " + matrix[0][1] + '\n' + matrix[1][0] + " " + matrix[1][1] + '\n';
   }


   public void mult (Vector vec) {
       vec.x = matrix[0][0] * vec.x + matrix[0][1] * vec.y;
       vec.y = matrix[1][0] * vec.x + matrix[1][1] * vec.y;
   }
}
