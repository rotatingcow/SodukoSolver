package exactcover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import hu.webarticum.treeprinter.printer.traditional.TraditionalTreePrinter;


class ExactCovers {


    public static void main(String[] args) {
        int[][] matrix = {
        {1, 0, 1, 0, 0, 0, 1, 0, 1, 0},
        {1 ,1 ,0 ,0 ,1 ,1 ,0, 0, 0, 0},
        {0 ,0, 0, 1, 0, 0, 0, 1, 0, 0},
        {0 ,1 ,0 ,1 ,1 ,0 ,0 ,0 ,0 ,0},
        {0 ,0 ,0 ,0 ,0 ,1 ,0 ,1 ,0 ,1},
        {1, 0, 1, 0, 0, 0, 1, 0, 1, 0},
        {1 ,1 ,0 ,0 ,1 ,1 ,0, 0, 0, 0},
        {0 ,0, 0, 1, 0, 0, 0, 1, 0, 0},
        {0 ,1 ,0 ,1 ,1 ,0 ,0 ,0 ,0 ,0},
        {0 ,0 ,0 ,0 ,0 ,1 ,0 ,1 ,0 ,1}
        };

        int[][] matrix2 = {
            {1, 0, 0, 1, 0, 0, 1},
            {1, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 1, 0, 0},
            {0, 0, 1, 0, 1, 1, 0},
            {0, 1, 1, 0, 0, 1, 1},
            {0, 1, 0, 0, 0, 0, 1},
        };


        boolean uhsd  = true;
        
        
        

        if(uhsd){
             ExactCovers exactCovers2 = new ExactCovers(matrix2);
             System.out.println("\n\n");
            ArrayList<List<Integer>> exactCover2 = exactCovers2.findExactCover();
            for (List<Integer> row : exactCover2) {
                System.out.println(Arrays.toString(row.toArray()));
            }
        }else{
            ExactCovers exactCovers = new ExactCovers(matrix);
            System.out.println("\n\n");
            ArrayList<List<Integer>> exactCover = exactCovers.findExactCover();
            for (List<Integer> row : exactCover) {
                System.out.println(Arrays.toString(row.toArray()));
            }
        }
        

       
        
        
        
    }

    public MatrixTree rootMatrix;

    public int numChildren = 0;

    enum SliceTypes {
        Column,
        Row
    }

    public ExactCovers(int[][] matrix) {
        List<List<Integer>> listMatrix = new ArrayList<List<Integer>>();
        for (int[] row : matrix) {
            listMatrix.add(Arrays.asList(ArrayUtils.toObject(row)));
        }
        rootMatrix = new MatrixTree(listMatrix);
    }

    public ArrayList<List<Integer>> findExactCover() {

        System.out.println("\nrootMatrix: \n"+rootMatrix);
        int targetCol = rootMatrix.findLowestColumn();

        int[] initRows = rootMatrix.getIndexesFromSlice(targetCol, SliceTypes.Column);

        
        ArrayList<ArrayList<Integer>> exactCoverSets = new ArrayList<ArrayList<Integer>>();

        
        for (int i = 0; i < initRows.length; i++) {
            rootMatrix.addChild(rootMatrix);
            numChildren++;
            ArrayList<Integer> exactCoverSet = new ArrayList<Integer>();
            try {
                List<Integer> product = recursiveSearch(rootMatrix.children.get(i), initRows[i]);    
                exactCoverSet.addAll(product);
                exactCoverSets.add(exactCoverSet);
                break;
            } catch (NullPointerException e) {
                
            }

        }
        if (exactCoverSets.size() == 0) {
            System.out.println("No exact covers exist for this set");
        }


        ArrayList<List<Integer>> exactCover = new ArrayList<List<Integer>>();

        for (ArrayList<Integer> exactCoverSet : exactCoverSets) {
            System.out.println("Exact cover = " + Arrays.toString(exactCoverSet.toArray()));
            for(int i = 0; i < rootMatrix.getMatrix().size(); i++){
                if(exactCoverSet.contains(i)){
                    exactCover.add(rootMatrix.getMatrix().get(i));
                }
            }
        }

        new TraditionalTreePrinter().print(rootMatrix);
        return exactCover;
        
        
    }

    List<Integer> recursiveSearch(MatrixTree tree, int row) {
        
        ArrayList<Integer> coverRows = new ArrayList<Integer>();
        int realRow = tree.getRowByIndex(row);
        

        int[] positiveColumns = tree.getIndexesFromSlice(row, SliceTypes.Row);

        ArrayList<Integer> positiveRows = new ArrayList<>();
        for (int column : positiveColumns) {
            for (Integer index : tree.getIndexesFromSlice(column, SliceTypes.Column)) {
                if(!positiveRows.contains(index)){
                    positiveRows.add(index);
                }
            }
        }

        positiveRows.sort(Comparator.naturalOrder());

        

       

        tree.removeSlice(positiveColumns, SliceTypes.Column);
        tree.removeSlice(positiveRows.stream().mapToInt(Integer::valueOf).toArray(), SliceTypes.Row);


        if (tree.matrix.size() == 0) {
            coverRows.add(realRow);
            return coverRows;
        }

        

        int lowestColumn = tree.findLowestColumn();
      

        if (lowestColumn == -1) {
            return null;
        }

        int[] searchRows = tree.getIndexesFromSlice(lowestColumn, SliceTypes.Column);

        
       

        for (int i = 0; i < searchRows.length; i++) {
            tree.addChild(tree);
            numChildren++;
            coverRows.add(realRow);
            try {
                List<Integer> product = recursiveSearch(tree.children.get(i), searchRows[i]);
                for (Integer integer : product) {
                    if(!coverRows.contains(integer)){
                        coverRows.add(integer);
                    }
                   
                }
                break;
            } catch (NullPointerException e) {
                
            }
        }

        return coverRows;
    }


}