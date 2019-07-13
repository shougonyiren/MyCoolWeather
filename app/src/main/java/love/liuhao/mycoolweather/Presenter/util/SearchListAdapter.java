package love.liuhao.mycoolweather.Presenter.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import love.liuhao.mycoolweather.MySearchItemRecyclerViewAdapter;
import love.liuhao.mycoolweather.R;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private List<Basic> mValues;

    public List<Basic> getmValues() {
        return mValues;
    }

    public SearchListAdapter(List<Basic> mValues, OnItemClickListener onItemClickListener) {
        this.mValues = mValues;
        this.onItemClickListener = onItemClickListener;
    }

    public void setmValues(List<Basic> mValues) {
        this.mValues = mValues;
    }

    public SearchListAdapter(List<Basic> basics) {
        this.mValues = mValues;
    }

    @NonNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchfragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String location=mValues.get(position).getLocation()+","+mValues.get(position).getAdmin_area();
        holder.mIdView.setText(location);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    //点击 RecyclerView 某条的监听
    public interface OnItemClickListener{

        /**
         * 当RecyclerView某个被点击的时候回调
         * @param position
         */
        void onItemClick(int position);

    }

    private OnItemClickListener onItemClickListener;

    /**
     * 设置RecyclerView某个的监听
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        if(mValues==null){
            return 0;
        }else {
            return mValues.size();
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public Basic mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_city);
            //添加点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(getLayoutPosition());
                    }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString()  + "'";
        }
    }
}
