package com.gio.currencyrate.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gio.currencyrate.R;
import com.gio.currencyrate.networking.Currency;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * Created by User24 on 27.04.2017.
 * Gio
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CategoryItemViewHolder> {

    private List<Currency> currencyList;
    private final WeakReference<Activity> ratesActivityReference;

    public RecyclerViewAdapter(Activity activity, List<Currency> currencyList) {
        ratesActivityReference = new WeakReference<>(activity);
        this.currencyList = currencyList;
    }

    @Override
    public CategoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.valute_item, parent, false);
        return new CategoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryItemViewHolder holder, int position) {

        final Currency currency = currencyList.get(position);

        // отображаем данные
        holder.tvNumCode.setText(currency.getNominal().toString());
        holder.tvName.setText(currency.getName().toString());
        holder.tvCode.setText(String.format("%s (%d)", currency.getCharCode(), currency.getNumCode()));
        holder.tvValue.setText(currency.getValue().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final boolean[] standardCalc = {true};
                final Activity activity = ratesActivityReference.get();
                if (activity != null) {
                    final View view = View.inflate(activity, R.layout.calculate_dialog, null);
                    final TextView tvCharCodeFrom = (TextView) view.findViewById(R.id.tvCharCodeFrom);
                    final TextView tvCharCodeTo = (TextView) view.findViewById(R.id.tvCharCodeTo);
                    final TextView tvValueTo = (TextView) view.findViewById(R.id.tvValueTo);
                    final EditText etValueFrom = (EditText) view.findViewById(R.id.etValueFrom);
                    ImageButton ivSwap = (ImageButton) view.findViewById(R.id.ivSwap);

                    tvCharCodeFrom.setText(R.string.valute_calculate_param);
                    tvCharCodeTo.setText(currency.getCharCode());

                    etValueFrom.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {
                        }

                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!etValueFrom.getText().toString().equals("")) {
                                if (standardCalc[0]) {
                                    int fromValue = Integer.parseInt(etValueFrom.getText().toString());
                                    tvValueTo.setText(String.valueOf(fromValue *
                                            currency.getNominal() / currency.getValue()));
                                } else {
                                    int fromValue = Integer.parseInt(etValueFrom.getText().toString());
                                    tvValueTo.setText(String.valueOf(fromValue *
                                            currency.getValue() / currency.getNominal()));
                                }
                            } else tvValueTo.setText("");
                        }
                    });

                    ivSwap.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (standardCalc[0]) {
                                tvCharCodeFrom.setText(currency.getCharCode());
                                tvCharCodeTo.setText(R.string.valute_calculate_param);
                                etValueFrom.setText("");
                                tvValueTo.setText("");
                                standardCalc[0] = false;
                            } else {
                                tvCharCodeFrom.setText(R.string.valute_calculate_param);
                                tvCharCodeTo.setText(currency.getCharCode());
                                etValueFrom.setText("");
                                tvValueTo.setText("");
                                standardCalc[0] = true;
                            }
                        }
                    });

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);

                    dialogBuilder.setView(view)
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    dialogBuilder.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }


    // view holder class ======================
    class CategoryItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvNumCode;
        TextView tvName;
        TextView tvCode;
        TextView tvValue;

        CategoryItemViewHolder(View itemView) {
            super(itemView);

            tvNumCode = (TextView) itemView.findViewById(R.id.tvNumCode);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCode = (TextView) itemView.findViewById((R.id.tvCode));
            tvValue = (TextView) itemView.findViewById(R.id.tvValue);
        }
    }
}