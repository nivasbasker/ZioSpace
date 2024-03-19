package com.zio.ziospace.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ModelCourse implements Parcelable {

    String cid, ctitle, cdesc, cdetail, cimage, csdate, cedate, cprice, clink, cmentor, category;
    boolean isVisible;

    public ModelCourse() {
    }

    public ModelCourse(String cid, String ctitle, String cdesc, String cdetail, String cimage,
                       String csdate, String cedate, String cprice, String clink, String cmentor, String category, boolean isVisible) {

        this.cid = cid;
        this.cprice = cprice;
        this.ctitle = ctitle;
        this.cdesc = cdesc;
        this.cdetail = cdetail;
        this.cimage = cimage;
        this.csdate = csdate;
        this.cedate = cedate;
        this.clink = clink;
        this.cmentor = cmentor;
        this.category = category;
        this.isVisible = isVisible;
    }


    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCtitle() {
        return ctitle;
    }

    public void setCtitle(String ctitle) {
        this.ctitle = ctitle;
    }

    public String getCdesc() {
        return cdesc;
    }

    public void setCdesc(String cdesc) {
        this.cdesc = cdesc;
    }

    public String getCdetail() {
        return cdetail;
    }

    public void setCdetail(String cdetail) {
        this.cdetail = cdetail;
    }

    public String getCimage() {
        return cimage;
    }

    public void setCimage(String cimage) {
        this.cimage = cimage;
    }

    public String getCsdate() {
        return csdate;
    }

    public void setCsdate(String csdate) {
        this.csdate = csdate;
    }

    public String getCedate() {
        return cedate;
    }

    public void setCedate(String cedate) {
        this.cedate = cedate;
    }

    public String getCprice() {
        return cprice;
    }

    public void setCprice(String cprice) {
        this.cprice = cprice;
    }

    public String getClink() {
        return clink;
    }

    public void setClink(String clink) {
        this.clink = clink;
    }

    public String getCmentor() {
        return cmentor;
    }

    public void setCmentor(String cmentor) {
        this.cmentor = cmentor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(cid);
        parcel.writeString(ctitle);
        parcel.writeString(cdesc);
        parcel.writeString(cdetail);
        parcel.writeString(cimage);
        parcel.writeString(csdate);
        parcel.writeString(cedate);
        parcel.writeString(cprice);
        parcel.writeString(clink);
        parcel.writeString(cmentor);
        parcel.writeString(category);

    }


    public static final Parcelable.Creator<ModelCourse> CREATOR
            = new Parcelable.Creator<ModelCourse>() {
        public ModelCourse createFromParcel(Parcel in) {
            return new ModelCourse(in);
        }

        public ModelCourse[] newArray(int size) {
            return new ModelCourse[size];
        }
    };

    private ModelCourse(Parcel in) {
        cid = in.readString();
        ctitle = in.readString();
        cdesc = in.readString();
        cdetail = in.readString();
        cimage = in.readString();
        csdate = in.readString();
        cedate = in.readString();
        cprice = in.readString();
        clink = in.readString();
        cmentor = in.readString();
        category = in.readString();
    }
}
