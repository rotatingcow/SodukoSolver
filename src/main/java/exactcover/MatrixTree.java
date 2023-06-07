package exactcover;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import exactcover.ExactCovers.SliceTypes;

import hu.webarticum.treeprinter.*;
import hu.webarticum.treeprinter.text.ConsoleText;

class MatrixTree implements TreeNode{

    public List<MatrixTree> children;

    private ArrayList<Integer> rows = new ArrayList<Integer>();
    private ArrayList<Integer> cols = new ArrayList<Integer>();

    public List<List<Integer>> matrix;

    public MatrixTree(List<List<Integer>> matrix){
        this.matrix = matrix;
        children = new ArrayList<MatrixTree>();
        
        for(int i = 0; i < matrix.size(); i++){
            rows.add(i);
        }
        for(int i = 0; i < matrix.get(0).size(); i ++){
            cols.add(i);
        }
    }

    public MatrixTree(MatrixTree parent){
        this.matrix = parent.getMatrix();
        children = new ArrayList<MatrixTree>();

        this.rows = new ArrayList<Integer>(parent.getRows());
        this.cols = new ArrayList<Integer>(parent.getCols());
    }

    public ArrayList<Integer> getRows(){
        return new ArrayList<Integer>(rows);
    }
    public ArrayList<Integer> getCols(){
        return new ArrayList<Integer>(cols);
    }

    public MatrixTree getChild(int index){
        return children.get(index);
    }

    public List<List<Integer>> getMatrix(){
        return new ArrayList<List<Integer>>(matrix);
    }

    public int getRowByIndex(int index){
        return rows.get(index);
    }

    public int getColByIndex(int index){
        return cols.get(index);
    }

    public void addChild(MatrixTree child){
        children.add(new MatrixTree(child));
    }

    public String toString(){
        String text = "";
        for(List<Integer> row : matrix){
            text += Arrays.toString(row.toArray())+"\n";
        }
        return text;
    }

    public void removeSlice(int[] indexes, SliceTypes type) {
        ArrayList<ArrayList<Integer>> newMatrix = new ArrayList<ArrayList<Integer>>();


        for (List<Integer> row : matrix) {
            ArrayList<Integer> addRow = new ArrayList<Integer>();
            addRow.addAll(row);
            newMatrix.add(addRow);
        }

        if (type == SliceTypes.Column) {
            for (int i = indexes.length-1; i >= 0; i --) {
                
                cols.remove((int)indexes[i]);
                for (int j = 0; j < newMatrix.size(); j++) {
                    newMatrix.get(j).remove(indexes[i]);
                }
            }
        } else {
            
            for (int i = indexes.length-1; i >= 0 ; i --) {
                rows.remove((int)indexes[i]);
                newMatrix.remove(indexes[i]);
            }
        }

        

        List<List<Integer>> finalMatrix = new ArrayList<>();
        for(ArrayList<Integer> row : newMatrix){
            Integer[] intRow = Arrays.stream(row.stream().mapToInt(i -> i).toArray()).boxed().toArray(Integer[]::new);
            finalMatrix.add(Arrays.asList(intRow));
        }

        matrix = finalMatrix;
    }


    public int[] getIndexesFromSlice(int index, SliceTypes type) {
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        int length = (type == SliceTypes.Column) ? matrix.size() : matrix.get(0).size();

        for (int j = 0; j < length; j++) {
            if (type == SliceTypes.Column) {
                if (matrix.get(j).get(index) == 1) {
                    indexes.add(j);
                }
            } else if (matrix.get(index).get(j) == 1) {
                indexes.add(j);
            }

        }

        return indexes.stream().mapToInt(i -> i).toArray();
    }


    int findLowestColumn() {
        Map<Integer, Integer> columnCounts = new HashMap<Integer, Integer>();

        for (int i = 0; i < matrix.get(0).size(); i++) {
            columnCounts.put(i, 0);
        }


        for(int i = 0; i < matrix.size(); i++){
            for(int j = 0; j < matrix.get(0).size(); j ++){
                if(matrix.get(i).get(j) == 1){
                    columnCounts.put(j, columnCounts.get(j) + 1);
                }
            }

        }

        int minValue = columnCounts.get(0);
        int minValueIndex = 0;

        for (int i = 0; i < columnCounts.size(); i++) {
            if (columnCounts.get(i) < minValue) {
                minValue = columnCounts.get(i);
                minValueIndex = i;
            }
        }
        if (columnCounts.get(minValueIndex) == 0) {
            return -1;
        }
        return minValueIndex;
    }


    public void executeChildren(){
        System.out.println("oh god no");
    }

    @Override
    public ConsoleText content() {
        String text = "";
        for(List<Integer> row : matrix){
            text += Arrays.toString(row.toArray())+"\n";
        }
        return ConsoleText.of(text);
    }

    @Override
    public List<TreeNode> children() {
        return children.stream().map((c) -> (TreeNode)c).toList();
    }

}