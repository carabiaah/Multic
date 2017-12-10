package com.watchthisspace.multic.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.watchthisspace.multic.R;
import com.watchthisspace.multic.objects.Player;

import java.util.List;

public class ScoreboardAdapter extends ArrayAdapter<Player> {

    private Context mContext;

    private List<Player> mPlayers;

    private static class ViewHolder {
        TextView mTextViewName;
    }

    public ScoreboardAdapter(Context context, List<Player> players) {
        super(context, R.layout.scoreboard_item, players);

        mContext = context;
        mPlayers = players;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        Player player = mPlayers.get(position);

        if(convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.scoreboard_item, parent, false);

            viewHolder.mTextViewName = convertView.findViewById(R.id.scoreboard_item_tv_name);
            viewHolder.mTextViewName.setText(player.getName());
        }

        return convertView;
    }
}
