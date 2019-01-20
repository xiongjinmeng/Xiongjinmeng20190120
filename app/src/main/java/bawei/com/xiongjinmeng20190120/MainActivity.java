package bawei.com.xiongjinmeng20190120;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import bawei.com.xiongjinmeng20190120.adapter.MoyAdapter;
import bawei.com.xiongjinmeng20190120.bean.MoListBean;
import bawei.com.xiongjinmeng20190120.presenter.Presenter;
import bawei.com.xiongjinmeng20190120.util.Intrifer;
import bawei.com.xiongjinmeng20190120.view.IView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements IView {

    @BindView(R.id.btn_name)
    Button btnName;
    @BindView(R.id.recycler_view)
    ListView recyclerView;
    private Presenter presenter;
    private Unbinder unbinder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        presenter = new Presenter(this);
        presenter.getMoter(Intrifer.QASTY, MoListBean.class);
        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
        unbinder.unbind();
    }

    @Override
    public void onSuccessful(Object data) {
        if (data instanceof MoListBean) {
            MoListBean bean = (MoListBean) data;
            final List<MoListBean.DataBean> list = bean.getData();
            final MoyAdapter adapter = new MoyAdapter(MainActivity.this, list);
            recyclerView.setAdapter(adapter);
            recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                            final String s = list.get(position).getPid() + "";
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MainActivity.this,ShopActivity.class);
                                    intent.putExtra("pid",s);
                                    startActivity(intent);
                                }
                            });

                }
            });
            recyclerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
                    final View view1 = adapter.getView(position, view, parent);
                    final ObjectAnimator animator = ObjectAnimator.ofFloat(view1, "alpha", 1f, 0f);
                    animator.setDuration(2000);
                    animator.start();
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            list.remove(position);
                            adapter.notifyDataSetChanged();
                            ObjectAnimator animat = ObjectAnimator.ofFloat(view1, "alpha", 0f, 1f);
                            animat.setDuration(200);
                            animat.start();
                        }
                    });


                    return true;
                }
            });
        }

    }

    @Override
    public void onEvester(String s) {
        Log.e("------", s);
    }
}