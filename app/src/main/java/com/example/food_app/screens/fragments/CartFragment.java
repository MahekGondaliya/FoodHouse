package com.example.food_app.screens.fragments;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.food_app.CartDao;
import com.example.food_app.CartDatabase;
import com.example.food_app.R;
import com.example.food_app.adapter.CartAdapter;
import com.example.food_app.model.CategoryModel;
import com.example.food_app.model.OrderModel;
import com.example.food_app.serverconfig.FirebaseHelperForOrder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private LinearLayout llOrders;

    private LottieAnimationView loadingOrderSucess;

    private TextView cartHide;
    private final List<CategoryModel> categoryEntityList = new ArrayList<>();
    private AppCompatButton btnPlaceOrder;
    private TextView SubTotal, total;
    private CartDatabase dbcart;
    private ExecutorService executorService;
    private final Handler handler = new Handler();
    private Runnable updateTotalRunnable;

    public CartFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        SubTotal = view.findViewById(R.id.SubTotal);
        total = view.findViewById(R.id.total);
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder);
        cartHide = view.findViewById(R.id.cartHide);
        llOrders = view.findViewById(R.id.llOrders);
        loadingOrderSucess = view.findViewById(R.id.loadingOrderSucess);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        cartAdapter = new CartAdapter(categoryEntityList, cartHide);
        recyclerView.setAdapter(cartAdapter);

        executorService = Executors.newSingleThreadExecutor();

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConformationDialog();
            }
        });

        loadCartItems();
        startUpdatingTotal();



        return view;
    }

    private void saveDataToFireBase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.e("CartFragment", "User not logged in");
            return;
        }

        OrderModel order = new OrderModel(user.getUid());
        order.addNestedList(new ArrayList<>(categoryEntityList));

        FirebaseHelperForOrder.saveModelToFirebase(order);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 loadingOrderSucess.setVisibility(View.GONE);

                Toast.makeText(getContext(), "Order Placed Successfully!", Toast.LENGTH_SHORT).show();

                // Replace current fragment with OrderFragment
                OrderFragment orderFragment = new OrderFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, orderFragment); // Ensure you have a FrameLayout with this ID
                transaction.addToBackStack(null); // Enables back navigation

                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bnView);
                bottomNavigationView.setSelectedItemId(R.id.order);
                transaction.commit();
            }
        },2000);


    }


    private void loadCartItems() {
        dbcart = CartDatabase.getInstance(requireContext());
        CartDao cartDao = dbcart.cartDao();

        executorService.execute(() -> {
            List<CategoryModel> items = cartDao.getAllRecords();
            requireActivity().runOnUiThread(() -> {
                if (items.isEmpty()){
                    cartHide.setVisibility(View.VISIBLE);
                }
                if (items != null && !items.isEmpty()) {
                    categoryEntityList.clear();
                    categoryEntityList.addAll(items);
                    cartAdapter.notifyDataSetChanged();
                } else {

                    Log.e("CartFragment", "Cart is empty or failed to load");
                }
            });
        });
    }

    private void startUpdatingTotal() {
        updateTotalRunnable = new Runnable() {
            @Override
            public void run() {
                if (isAdded() && dbcart != null) {
                    CartDao cartDao = dbcart.cartDao();
                    executorService.execute(() -> {
                        Double totalPrice = cartDao.getTotalPrice();
                        requireActivity().runOnUiThread(() -> {
                            if (isAdded()) {
                                if (totalPrice != null) {
                                    SubTotal.setText(String.format("$%.2f", totalPrice));
                                    double total_gst = totalPrice + (totalPrice * 0.05);
                                    total.setText(String.format("$%.2f", total_gst));
                                } else {
                                    SubTotal.setText("$0.00");
                                    total.setText("$0.00");
                                }
                            }
                        });
                    });

                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(updateTotalRunnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(updateTotalRunnable);
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private void showConformationDialog() {
        // Create the dialog
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_order_confirmation);
        dialog.setCancelable(false);

        // Get the views

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);


        // Apply the fade-in animation to the dialog
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(dialog.getWindow().getDecorView(), "alpha", 0f, 1f);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeIn.setDuration(500);
        fadeIn.start();

        // Button actions
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            loadingOrderSucess.setVisibility(View.VISIBLE);
            saveDataToFireBase();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    dbcart.cartDao().deleteAllCategories();
                }
            });
            dialog.dismiss();
        });


        dialog.show();
    }
}
