package mobi.vhly.xutilsdemo;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.xutils.DbManager;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.SqlInfoBuilder;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

import mobi.vhly.xutilsdemo.model.Book;

public class BookManageActivity extends AppCompatActivity {

    @ViewInject(R.id.txt_show)
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manage);

        x.view().inject(this);


        // xUtils 3 对象关系映射  ORM
        DbManager.DaoConfig config = new DbManager.DaoConfig();
        config.setDbName("app.db").setDbVersion(1);
        DbManager db = x.getDb(config);

        Book book = new Book();
        book.setTitle("10天学会Android");
        book.setAuthor("Dr. Zhang");
        book.setPrice(300.0f);

        try {
//            db.saveBindingId(book);
//
//            Snackbar.make(
//                    mTextView,
//                    "添加图书" + book.getId(),
//                    Snackbar.LENGTH_SHORT
//            ).show();

            // 查找所有Book表中的记录，并且形成 对象
            List<Book> books = db.findAll(Book.class);
            for (Book bk : books) {
                System.out.println("bk = " + bk.getId());
            }

            // 按照条件查询
            SqlInfo sql = new SqlInfo("select * from books where _id > 2");
            List<DbModel> dbModelAll = db.findDbModelAll(sql);
            if (dbModelAll != null) {
                for (DbModel dbModel : dbModelAll) {
                    long id = dbModel.getLong("_id");
                    System.out.println("id = " + id);
                }
            }

            Book bk = db.findById(Book.class, 4);

            // 删除

            // 删除数据库表 Book中所有的记录
//            db.delete(Book.class);

            // 删除特定的一个数据对象代表的记录
//            db.delete(bk);

            // WhereBuild 可以定义复杂的where条件
            // "_id" ">"  "3"
            WhereBuilder builder = WhereBuilder.b("_id", ">", 3);
            int n = db.delete(Book.class, builder);
            if(n > 0){
                Snackbar.make(mTextView, "删除成功", Snackbar.LENGTH_SHORT).show();
            }

            // 更新
//            db.update()

        } catch (DbException e) {
            e.printStackTrace();
        } finally {
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
