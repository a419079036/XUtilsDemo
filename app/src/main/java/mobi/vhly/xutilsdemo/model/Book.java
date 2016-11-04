package mobi.vhly.xutilsdemo.model;

import android.support.annotation.ColorInt;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by vhly[FR].
 * <p>
 * Author: vhly[FR]
 * Email: vhly@163.com
 * Date: 2016/10/21
 */
@Table(name = "books")
public class Book {

    /**
     * 主键，自增 _id
     */
    @Column(name = "_id",  isId = true)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "price")
    private float price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
