package com.zhonghong.xqshijie.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Flowlayout extends ViewGroup {
    private int horizontolSpacing = 20;
    private int verticalSpacing = 20;

    public Flowlayout(Context context) {
        super(context);
    }

    public void setHorizontolSpacing(int horizontolSpacing) {
        this.horizontolSpacing = horizontolSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    // 分配孩子的位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            line.layout(left, top);
            top = top + line.getHeight() + verticalSpacing; // 下一行分配起点坐标计算出来
        }
    }

    private List<Line> lines = new ArrayList<Flowlayout.Line>();
    private Line line;// 当前的行
    int userSize = 0;// 当前行使用的空间

    // 测量控件的大小 , 作为一个容器 有义务测量孩子的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 实际可以用的宽和高
        int height = MeasureSpec.getSize(heightMeasureSpec)
                - getPaddingBottom() - getPaddingTop();
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                - getPaddingRight();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec); // 精确的值 , 最大是多大
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 测量孩子
        restoreLine();  //把之前的数据都清空
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childwidthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
                    widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST
                            : widthMode);
            int childheightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST
                            : heightMode);
            child.measure(childwidthMeasureSpec, childheightMeasureSpec);
            if (line == null) {
                line = new Line();  // 创建新一行
            }
            int measuredWidth = child.getMeasuredWidth();
            userSize += measuredWidth;
            if (userSize <= width) { // 如果使用的宽度 小于可用的宽度 这时候 孩子能够添加到当前的行上
                line.addChild(child);
                userSize += horizontolSpacing;// 给孩子后面加上一个间距
                if (userSize > width) {
                    newLine();
                }

            } else {
                // 换行
                if (line.getChildCount() > 0) {
                    newLine();
                    line.addChild(child);
                    userSize += child.getMeasuredWidth();
                    userSize += horizontolSpacing;
                } else {
                    // 保证行上面最少有一个孩子
                    line.addChild(child);
                    newLine();
                }
            }
        }
        // 把最后一行记录到集合中
        if (line != null && !lines.contains(line)) {
            lines.add(line);
        }
        int totalheight = 0;

        // 把所有行的高度 加上
        for (int i = 0; i < lines.size(); i++) {
            totalheight += lines.get(i).getHeight();
        }
        // 把行的间距加上 数量 行的数量-1
        totalheight += verticalSpacing * (lines.size() - 1);
        // 加上上下pading
        totalheight += getPaddingBottom();
        totalheight += getPaddingTop();
        // 设置自身尺寸
        // 设置布局的宽高，宽度直接采用父view传递过来的最大宽度，而不用考虑子view是否填满宽度，因为该布局的特性就是填满一行后，再换行
        // 高度根据设置的模式来决定采用所有子View的高度之和还是采用父view传递过来的高度
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                resolveSize(totalheight, heightMeasureSpec));
    }

    private void restoreLine() {
        lines.clear();
        line = new Line();
        userSize = 0;
    }

    private void newLine() {
        // 把之前的行记录下来
        if (line != null) {
            lines.add(line);
        }
        // 创建新的一行
        line = new Line();
        userSize = 0;
    }

    /**
     * 封装行的对象 管理每个行上view对象
     */
    class Line {
        private List<View> children = new ArrayList<View>();
        int height;
        int width;

        public void addChild(View child) {
            children.add(child);
            if (height < child.getMeasuredHeight()) { // 让当前的行的高度 是最高的一个孩子的高度
                height = child.getMeasuredHeight();
                System.out.println(child.getMeasuredHeight());
            }
            width = 0;
            for (int i = 0; i < children.size(); i++) {
                width += children.get(i).getMeasuredWidth();
            }

            width += (children.size() - 1) * horizontolSpacing;
        }

        public void layout(int left, int top) {
            int totalWidth = getMeasuredWidth() - getPaddingLeft()
                    - getPaddingRight();
//			int surplusWidth = totalWidth - width; // 获取到了剩余的宽度
            int surplusWidth = 0; // 不分配剩余宽度
            int childSurplus = 0;
            if (children.size() > 0){
                childSurplus = surplusWidth / children.size(); // 每个孩子额外分配的宽度
            }

            // 遍历所有行上的孩子 分配每个孩子的位置
            int l = left;
            for (int i = 0; i < children.size(); i++) {
                View view = children.get(i);
                if (childSurplus > 0) { // 重新 测量孩子
                    int childWidth = view.getMeasuredWidth() + childSurplus; // 由于每个孩子的宽度发生变化了,
                    int childHeight = view.getMeasuredHeight();
                    int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                            childWidth, MeasureSpec.EXACTLY);
                    int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                            childHeight, MeasureSpec.EXACTLY);
                    view.measure(widthMeasureSpec, heightMeasureSpec); // 要想让变化生效必须冲洗测量该孩子
                }

                view.layout(l, top, l + view.getMeasuredWidth(),
                        top + view.getMeasuredHeight());
                l = l + view.getMeasuredWidth() + horizontolSpacing;
            }
        }

        public int getHeight() {
            return height;
        }

        public int getChildCount() {
            return children.size();
        }
    }

}
