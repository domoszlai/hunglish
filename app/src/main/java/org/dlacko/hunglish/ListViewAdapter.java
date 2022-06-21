package org.dlacko.hunglish;

import android.content.Context;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

import org.dlacko.hunglish.query.SentencePair;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final ArrayList<SentencePair> arraylist;

    public ListViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<>();
    }

    // Probably a bit of an overkill
    private static class ViewHolder {
        TextView huSentence;
        TextView enSentence;
        TextView genre;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public SentencePair getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_view_items, null);

            holder.huSentence = convertView.findViewById(R.id.huSentence);
            holder.enSentence = convertView.findViewById(R.id.enSentence);
            holder.genre = convertView.findViewById(R.id.genre);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.huSentence.setText(
                HtmlCompat.fromHtml(
                        arraylist.get(position).getHuSentence().asHtml(),
                        HtmlCompat.FROM_HTML_MODE_LEGACY));

        holder.enSentence.setText(
                HtmlCompat.fromHtml(
                        arraylist.get(position).getEnSentence().asHtml(),
                        HtmlCompat.FROM_HTML_MODE_LEGACY));

        try {
            holder.genre.setText(arraylist.get(position).getGenre().getTitle());
        } catch (Exception e) {
            // If genre cannot be found at least it does not crash
            holder.genre.setText("-");
        }

        return convertView;
    }

    public void addSentencePairs(List<SentencePair> results) {
        arraylist.addAll(results);
        notifyDataSetChanged();
    }

    public void clearSentencePairs(){
        arraylist.clear();
        notifyDataSetChanged();
    }
}