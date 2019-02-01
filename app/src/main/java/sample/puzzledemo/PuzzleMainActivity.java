package sample.puzzledemo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

import sample.puzzledemo.databinding.ActivityPuzzleMainBinding;
import sample.puzzledemo.model.RowColBean;

public class PuzzleMainActivity extends AppCompatActivity {

    private String TAG = PuzzleMainActivity.class.getSimpleName();
    Integer[][] field;
    private LinearLayout mLinearLayout;
    private View mView;
    private Context mContext;
    private int MAX_ROW_COUNT = 0;
    private int MAX_COLUMN_COUNT= 0 ;
    private ActivityPuzzleMainBinding mBinding;
    private ArrayList<RowColBean> mRowColList;
    private HashMap<Integer, ArrayList<RowColBean>> mRowHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_puzzle_main);
        getControls();
        mBinding.btnSolveForm.setOnClickListener(this::resultClick);
        mBinding.btnGeneratePuzzle.setOnClickListener(this::drawPuzzleclick);
        mBinding.btnGeneratePuzzle.setSelected(false);
    }

    private void getControls()
    {
        mContext = PuzzleMainActivity.this;
    }

    /*
    * draw puzzle with random values
    * */
    private void drawPuzzleclick(View view) {
        mBinding.btnGeneratePuzzle.setSelected(true);
        mBinding.btnGeneratePuzzle.setClickable(false);
        mBinding.btnGeneratePuzzle.setEnabled(false);
        mBinding.btnSolveForm.setClickable(true);
        mBinding.btnSolveForm.setEnabled(true);
        mBinding.btnSolveForm.setSelected(false);
        generateRandomNumbers();
        if (mBinding.llVertical != null && mBinding.llVertical.getChildCount() > 0)
            mBinding.llVertical.removeAllViews();
        setGridView();

    }

    /*
    * show the result
    * */
    private void resultClick(View view) {
        mBinding.btnGeneratePuzzle.setClickable(true);
        mBinding.btnGeneratePuzzle.setEnabled(true);
        mBinding.btnGeneratePuzzle.setSelected(false);
        mBinding.btnSolveForm.setClickable(false);
        mBinding.btnSolveForm.setEnabled(false);
        mBinding.btnSolveForm.setSelected(true);

        getMaxFormAreaRow();
        if(rowhighValueIndex>0) {
            getMaxFormArea();
        }
    }

    /*
    * generate random 2d array
    * */
    private void generateRandomNumbers()
    {
        field = new Integer[5][5];

        for(int i = 0 ;i <field.length;i++)
        {
            for(int j = 0 ; j <field[i].length;j++)
            {
                field[i][j] = Integer.parseInt(String.valueOf(Math.round( Math.random())));
            }
        }
        MAX_ROW_COUNT = field.length;
        MAX_COLUMN_COUNT = field[0].length;

    }

    /*
    * set grid view as per random array values
    * */
    private void setGridView() {
        for (int i = 0; i < field.length; i++) {
            mLinearLayout = new LinearLayout(mContext);
            mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
            mLinearLayout.setLayoutParams(param);
            for (int j = 0; j < field[i].length; j++) {
                mView = new View(mContext);
                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(150, 150);
                param1.setMargins(5, 5, 5, 5);
                mView.setLayoutParams(param1);
                if (field[i][j]==(0)) {
                    mView.setBackgroundColor(mContext.getResources().getColor(R.color.water));
                } else if (field[i][j]==(1)) {
                    mView.setBackgroundColor(mContext.getResources().getColor(R.color.land));
                }
                mLinearLayout.addView(mView);
            }
            mBinding.llVertical.addView(mLinearLayout);
        }
    }

    /*
    * get max sand area
    * */
    private void getMaxFormAreaRow() {
        int hashMapIndex2 = 1;
        mRowHashMap = new HashMap<>();
        mRowColList = new ArrayList<>();

        for (int i = 0; i < field.length; i++) {
//            tempCol = 0;
            //change row
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == 1) {
                    if (mRowHashMap.size() == 0) {

                        mRowColList.add(new RowColBean(i, j));
                        mRowHashMap.put(hashMapIndex2, mRowColList);
//                        tempCol ++;
                    } else {

                        //get index of hashmap value for arraylist
                        if (getHashIndexRow2(i,j) != -1) {
                            mRowColList = getlist2((getHashIndexRow2(i,j)));
                        } else {
                            mRowColList = new ArrayList<>();
                        }
                        mRowColList.add(new RowColBean(i, j));

                        //save same arraylist at correct index
                        if (getHashIndexRow2(i,j) != -1) {
                            mRowHashMap.put(getHashIndexRow2(i,j), mRowColList);
                        } else {
                            hashMapIndex2++;
                            mRowHashMap.put(hashMapIndex2, mRowColList);
                        }
                    }
                } else if (field[i][j] == 0) {
                    mRowColList = new ArrayList<>();
                }

               if(i>0 && i<=MAX_ROW_COUNT) {

                   if (field[i-1][j] == 1)
                    {
                        if (getHashIndexRow2(i, j) != -1) {
                            mRowColList = getlist2((getHashIndexRow2(i, j)));
                            mRowColList.add(new RowColBean(i - 1, j));
                            mRowHashMap.put(getHashIndexRow2(i, j), mRowColList);

                            if (j<MAX_COLUMN_COUNT-1 && field[i-1][j+1] == 1 && getHashIndexRow2(i, j) != -1)
                            {
                                mRowColList = getlist2((getHashIndexRow2(i, j)));
                                mRowColList.add(new RowColBean(i - 1, j+1));
                                mRowHashMap.put(getHashIndexRow2(i, j), mRowColList);
                            }

                            if (j>0 && j<=MAX_COLUMN_COUNT-1 && field[i-1][j-1] == 1 && getHashIndexRow2(i, j) != -1)
                            {
                                mRowColList = getlist2((getHashIndexRow2(i, j)));
                                mRowColList.add(new RowColBean(i - 1, j-1));
                                mRowHashMap.put(getHashIndexRow2(i, j), mRowColList);
                            }
                        }
                    }
                }

                if(i>0 && i<MAX_ROW_COUNT-1) {
                    if (field[i+1][j] == 1 &&  (getHashIndexRow2(i, j) != -1) ){
                        mRowColList = getlist2((getHashIndexRow2(i, j)));
                        mRowColList.add(new RowColBean(i+1, j));
                        mRowHashMap.put(getHashIndexRow2(i, j), mRowColList);
                    }
                }

                if(( j==0 || j<MAX_COLUMN_COUNT-1)  &&  (getHashIndexRow2(i, j) != -1)) {
                    if (field[i][j+1] == 1){
                        mRowColList = getlist2((getHashIndexRow2(i, j)));
                        mRowColList.add(new RowColBean(i, j+1));
                        mRowHashMap.put(getHashIndexRow2(i, j), mRowColList);
                    }
                }

                if(( j>0 && j<=MAX_COLUMN_COUNT-1)  &&  (getHashIndexRow2(i, j) != -1)) {
                    if (field[i][j-1] == 1){
                        mRowColList = getlist2((getHashIndexRow2(i, j)));
                        mRowColList.add(new RowColBean(i, j-1));
                        mRowHashMap.put(getHashIndexRow2(i, j), mRowColList);

                        if (j>=2 && field[i][j-2] == 1){
                            mRowColList = getlist2((getHashIndexRow2(i, j)));
                            mRowColList.add(new RowColBean(i, j-2));
                            mRowHashMap.put(getHashIndexRow2(i, j), mRowColList);
                        }
                    }
                }
            }
        }

        int max = 0;
        for (int i = 1; i < mRowHashMap.size() + 1; i++) {
            if (i == 1) {
                rowhighValueIndex = i;
                max = mRowHashMap.get(i).size();
            } else {
                if (max < mRowHashMap.get(i).size()) {
                    rowhighValueIndex = i;
                    max = mRowHashMap.get(i).size();
                }
            }
        }
    }

    /*
    * get hash map value index as per values exists
    * */
    private int getHashIndexRow2(int row, int col) {
        int returnValue = -1;
        for (int i = 1; i < mRowHashMap.size() + 1; i++) {
            if (mRowHashMap.get(i) != null) {
                ArrayList<RowColBean> list = mRowHashMap.get(i);
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).rowIndex == row && list.get(j).colIndex == col) {
                        return i;
                    }
                }
            }
        }
        return returnValue;
    }


    int rowhighValueIndex = -1;
    /*
    * get values
    * */
    private ArrayList<RowColBean> getlist2(int value) {
        if (mRowHashMap.get(value) != null) {
            return mRowHashMap.get(value);
        }
        return new ArrayList<>();
    }

    /*
    * show the largest sand area
    * */
    private void getMaxFormArea() {
        ArrayList<RowColBean> mMaxSandAreaList = new ArrayList<>();
            mBinding.llVertical.removeAllViews();
            mMaxSandAreaList = mRowHashMap.get(rowhighValueIndex);
            int listSize = mMaxSandAreaList.size();

            for (int i = 0; i < field.length; i++) {
                mLinearLayout = new LinearLayout(mContext);
                mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
                mLinearLayout.setLayoutParams(param);
                for (int j = 0; j < field[i].length; j++) {
                    mView = new View(mContext);
                    LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(150, 150);
                    param1.setMargins(5, 5, 5, 5);
                    mView.setLayoutParams(param1);

                    if (field[i][j] == (0)) {
                        mView.setBackgroundColor(mContext.getResources().getColor(R.color.water));
                    } else if (field[i][j] == (1)) {
                        mView.setBackgroundColor(mContext.getResources().getColor(R.color.land));
                    }

                    for (int k = 0; k < listSize; k++) {
                        int colIndex = mMaxSandAreaList.get(k).colIndex;
                        int rowIndex = mMaxSandAreaList.get(k).rowIndex;
                        if (colIndex == j && rowIndex == i) {
                            mView.setBackgroundColor(mContext.getResources().getColor(R.color.fill));
                        }
                    }
                    mLinearLayout.addView(mView);
                }
                mBinding.llVertical.addView(mLinearLayout);
            }
    }
}
