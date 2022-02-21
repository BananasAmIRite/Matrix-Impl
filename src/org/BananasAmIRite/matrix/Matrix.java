package org.BananasAmIRite.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matrix {
    private final List<List<Integer>> matrix;

    public Matrix(int rows, int cols) {
        this.matrix = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) row.add(null);
            this.matrix.add(row);
        }
    }

    public Matrix(List<List<Integer>> matrix) {
        this.matrix = matrix;
    }

    public Matrix(int[][] matrix) {
        this.matrix = convertArrayToList(matrix);
    }

    private List<List<Integer>> convertArrayToList(int[][] m) {
        return Arrays.stream(m)
                .map(a ->
                        Arrays.stream(a)
                                .boxed()
                                .toList()
                )
                .toList();
    }

    public int getRows() {
        return matrix.size();
    }

    public int getColumns() {
        try {
            return matrix.get(0).size();
        } catch (Exception ignored) {
            return 0;
        }
    }

    public List<Integer> getRow(int r) {
        return new ArrayList<>(matrix.get(r)); // we dont want to supply the raw matrix
    }

    public List<Integer> getColumn(int c) {
        return matrix.stream().map(e -> e.get(c)).toList();
    }

    public Integer getAt(int row, int col) {
        return matrix.get(row).get(col);
    }

    public void put(int row, int col, int val) {
        matrix.get(row).set(col, val);
    }

    public List<List<Integer>> getMatrix() {
        return matrix;
    }

    public Matrix add(Matrix m) {
        return add(this, m);
    }

    public static Matrix add(Matrix m1, Matrix m2) {
        if (m1.getColumns() != m2.getColumns() || m1.getRows() != m2.getRows()) throw new IllegalArgumentException("The # of rows or columns are not equal. ");

        Matrix m = new Matrix(m1.getRows(), m1.getColumns());

        for (int i = 0; i < m1.getRows(); i++) {
            for (int j = 0; j < m1.getColumns(); j++) {
                int v1 = m1.getAt(i, j);
                int v2 = m2.getAt(i, j);

                m.put(i, j, v1 + v2);
            }
        }

        return m;
    }

    public Matrix subtract(Matrix m) {
        return subtract(this, m);
    }

    public static Matrix subtract(Matrix m1, Matrix m2) {
        Matrix _m2 = new Matrix(m2.getMatrix().stream().map(e -> e.stream().map(f -> -f).toList()).toList());
        return add(m1, _m2);
    }


    public Matrix multiply(Matrix m) {
        return multiply(this, m);
    }

    public static Matrix multiply(Matrix m1, Matrix m2) {
        if (m1.getColumns() != m2.getRows()) throw new IllegalArgumentException("The # of columns does not equal the # of rows. ");

        int m1Rows = m1.getRows();
//        int m1Cols = m1.getColumns();
//        int m2Rows = m2.getRows();
        int m2Cols = m2.getColumns();

        // initialize an empty matrix
        Matrix m = new Matrix(m1Rows, m2Cols);

        for (int i = 0; i < m1Rows; i++) {
            List<Integer> m1Row = m1.getRow(i);
            for (int j = 0; j < m2Cols; j++) {
                List<Integer> m2Col = m2.getColumn(j);

                // sadly we cant use a reducer :(
                int val = 0;
                System.out.println(m1Row);
                for (int k = 0; k < m1Row.size(); k++) val += m1Row.get(k) * m2Col.get(k);
                m.put(i, j, val);
            }
        }

        return m;
    }

    public static Matrix kroneckerProduct(Matrix m1, Matrix m2) {
        Matrix m = new Matrix(m1.getRows() * m2.getRows(), m1.getColumns() * m2.getColumns());

        for (int m1R = 0; m1R < m1.getRows(); m1R++) {
            for (int m2R = 0; m2R < m2.getRows(); m2R++) {
                for (int m1C = 0; m1C < m1.getColumns(); m1C++) {
                    for (int m2C = 0; m2C < m2.getColumns(); m2C++) {

                    int v = m1.getAt(m1R, m1C);
                    int v2 = m2.getAt(m2R, m2C);
                    m.put(m2.getRows()*m1R+m2R, m2.getColumns()*m1C+m2C, v*v2);

                    }
                }

            }
        }

        return m;
    }

    @Override
    public String toString() {
        return "Matrix[\n" +
                String.join("\n", matrix.stream().map(e -> String.join(", ", e.toString())).toList())
                 + "\n]";
    }

    public static void main(String[] args) {
        Matrix m1 = new Matrix(new int[][] {
            new int[] {1, -4, 7},
                new int[] {-2, 3, 3}
        });
        Matrix m2 = new Matrix(new int[][] {
                new int[] {8, -9, -6, 5},
                new int[] {1, -3, -4, 7},
                new int[] {2, 8, -8, -3},
                new int[] {1, 2, -5, -1}
        });

        System.out.println(m1);
        System.out.println(m2);
        System.out.println(Matrix.kroneckerProduct(m1, m2));
    }
}
