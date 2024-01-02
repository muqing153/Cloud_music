package com.muqingbfq.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.muqingbfq.R;
import com.muqingbfq.mq.gj;
import com.muqingbfq.yc;

import java.util.ArrayList;
import java.util.List;

public class LrcView extends RecyclerView {

    public static List<LRC> lrclist = new ArrayList<>();

    View.OnClickListener onClickListener;

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        onClickListener = l;
        super.setOnClickListener(l);
    }

    public static class LRC {
        public String lrc, tlyric;
        public long time;

        public LRC(String lrc, long time) {
            this.lrc = lrc;
            this.time = time;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            LRC lrc = (LRC) obj;
            return time == lrc.time;
        }

        public LRC setTlyric(String str) {
            this.tlyric = str;
            return this;
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(time);
        }

    }

    public LrcView(Context context) {
        super(context);
        init();
    }

    AttributeSet attrs;
    boolean Lrcline;
    LinearLayoutManager linearLayoutManager;

    public LrcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        init();
    }

    public LrcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        init();
    }

    public void setTextColor(int textColor) {
        TextColor = textColor;
    }

    public void setTextColor(String textColor) {
        TextColor = Color.parseColor(textColor);
    }

    public void setLrcline(boolean lrcline) {
        Lrcline = lrcline;
    }

    private void init() {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.LrcView);
            TextColor = ta.getColor(R.styleable.LrcView_TextColor,
                    ContextCompat.getColor(getContext(), R.color.text));
            Lrcline = ta.getBoolean(R.styleable.LrcView_Lrcline, true);
            addOnGlobalLayoutListener = ta.
                    getBoolean(R.styleable.LrcView_addOnGlobalLayoutListener, false);
            ta.recycle();
        }
        linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(recyclerView.getContext());
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

        };
        setLayoutManager(linearLayoutManager);
        setAdapter(new adaper());
        setForeground(null);
        setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        if (!Lrcline) {
            addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    view.setOnClickListener(onClickListener);

                    int parentHeight = parent.getHeight();
                    int childHeight = view.getHeight();

                    int topMargin = (parentHeight - childHeight) / 2;

                    // 设置第一项的顶部间距
                    if (parent.getChildAdapterPosition(view) == 0) {
                        outRect.top = topMargin;
                    }
                }
            });
        } else {
            setOnTouchListener(null);
        }
    }

    private static class CenterSmoothScroller extends LinearSmoothScroller {

        CenterSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
        }

        @Override
        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return 100f / displayMetrics.densityDpi;
        }
    }

    public static void setLrc(String a, String b) {
        try {
            lrclist.clear();
            setLrc(a);
            setLrc(b);
        } catch (Exception e) {
            yc.start("LrcView setLrc :" + e + a + b);
        }
    }

    public static void setLrc(String a) {
        // 去除空格
        if (TextUtils.isEmpty(a)) {
            return;
        }
        a.trim();
        String[] lines = a.split("\n");
        for (String line : lines) {
            String[] parts = line.split("]");
            if (parts.length >= 2) {
                String timeString = parts[0].substring(1);
                String lyric = parts[1].trim();
                String[] timeParts = timeString.split(":");
                if (timeParts.length >= 2) {
                    int minute = Integer.parseInt(timeParts[0]);
                    String[] secondParts = timeParts[1].split("[.\\-]");
                    if (secondParts.length >= 2) {
                        int second = Integer.parseInt(secondParts[0]);
                        int millisecond = Integer.parseInt(secondParts[1]);
                        long time = (long) minute * 60 * 1000 + second * 1000L + millisecond;
                        LRC lrc = new LRC(lyric, time);
                        if (lrclist.contains(lrc)) {
                            int index = lrclist.indexOf(lrc);
                            lrclist.get(index).setTlyric(lyric);
                        } else {
                            lrclist.add(lrc);
                        }
                    }
                }
            }
        }
    }

    int index = -1;
    int size = 20;

    public void setSize(int size) {
        this.size = size;
    }

    float alpha = 1.0f;

    @Override
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    boolean addOnGlobalLayoutListener;

    class adaper extends RecyclerView.Adapter<VH> {

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_lrc, parent, false);
            TextView textView = inflate.findViewById(R.id.text);
            textView.setTextColor(TextColor);
            textView.setTextSize(
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, size,
                            getResources().getDisplayMetrics()));
            textView.setAlpha(alpha);
            inflate.setOnClickListener(LrcView.this.onClickListener);

            return new VH(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            if (Lrcline) {
                if (addOnGlobalLayoutListener) {
                    // 注册布局监听器
                    holder.textView.getViewTreeObserver().addOnGlobalLayoutListener(
                            new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    int height = holder.textView.getHeight();
                                    ViewGroup.LayoutParams layoutParams = LrcView.this.getLayoutParams();
                                    layoutParams.height = height * 5;
                                    LrcView.this.setLayoutParams(layoutParams);
                                    // 移除布局监听器
                                    holder.textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                            });
                }
                if (lrclist.isEmpty()) {
                    holder.textView.setText("纯音乐，请欣赏");
                    return;
                }
                int currentLineIndex = getCurrentLineIndex();
                if (currentLineIndex < lrclist.size()) {

                    String text;
                    if (lrclist.size() <= 3) {
                        for (LRC a : lrclist) {
                            if (a.time == 5940000 && a.lrc.equals("纯音乐，请欣赏")) {
                                text = "纯音乐，请欣赏";
                                holder.textView.setText(text);
                                return;
                            }
                        }
                    }
                    LRC currentLrc = lrclist.get(currentLineIndex);
                    text = currentLrc.lrc;
                    if (currentLrc.tlyric != null) {
                        text += "\n" + currentLrc.tlyric;
                    }
                    holder.textView.setText(text);
                }
            } else {
                if (lrclist.isEmpty()) {
                    holder.textView.setText("纯音乐，请欣赏");
                    return;
                }
                try {
                    LRC lrc = lrclist.get(position);
                    StringBuilder stringBuffer = new StringBuilder();
                    stringBuffer.append(lrc.lrc);
                    if (lrc.tlyric != null) {
                        stringBuffer.append("\n").append(lrc.tlyric);
                    }
                    stringBuffer.append("\n");
                    holder.textView.setAlpha(0.1f);
                    if (getCurrentLineIndex(time) == position) {
                        holder.textView.setAlpha(1.0f);
                    }
                    holder.textView.setText(stringBuffer.toString());
                } catch (Exception e) {
                    gj.sc("LrcView.ADAPER.onBindViewHolder" + e);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (lrclist.size() < 3) {
                for (LRC a : lrclist) {
                    if (a.time == 5940000 && a.lrc.equals("纯音乐，请欣赏")) {
                        return 1;
                    }
                }
            }
            if (Lrcline) {
                return 1;
            }
            if (lrclist.isEmpty()) {
                return 1;
            }
            return lrclist.size();
        }
    }

    class VH extends RecyclerView.ViewHolder {
        TextView textView;

        public VH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }

    private int getCurrentLineIndex() {
        index = 0;
        for (int i = 0; i < lrclist.size(); i++) {
            LRC lineLrc = lrclist.get(i);
            if (lineLrc.time <= time) {
                index = i;
            } else {
                break;
            }
        }
        return index;
    }

    private static int getCurrentLineIndex(long time) {
        int index = 0;
        for (int i = 0; i < lrclist.size(); i++) {
            LRC lineLrc = lrclist.get(i);
            if (lineLrc.time <= time) {
                index = i;
            } else {
                break;
            }
        }
        return index;
    }

    int TextColor;
    long time;

    @SuppressLint("NotifyDataSetChanged")
    public void setTimeLrc(long a) {
        this.time = a;
        if (!Lrcline) {
            int currentLineIndex = getCurrentLineIndex(a);
            getAdapter().notifyDataSetChanged();
            if (currentLineIndex < 3) {
                return;
            }
//            smoothScrollToPosition(getCurrentLineIndex(a));
            linearLayoutManager.smoothScrollToPosition(this,
                    new RecyclerView.State(), currentLineIndex);
            return;
        }
        getAdapter().notifyDataSetChanged();
    }
}
