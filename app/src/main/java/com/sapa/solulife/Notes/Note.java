package com.sapa.solulife.Notes;

/**
 * Created by Pooja S on 9/30/2016.
 */

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Note implements Serializable {
    private Long id;
    private String title;

    private int lock_status;
    private String note_status;

    private String content;
    private String updatedAt;
    private boolean isSelected;
    private boolean imgbtn;
    private String tag;
    private List<Note> notes;
    private int color;
    private String status;

    private byte[] image;

    private int favourite;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getFavourite(){
        return this.favourite;
    }

    public void setFavourite(int favourite){
        this.favourite = favourite;
    }


    public int getLock_status()
    {
        return this.lock_status;
    }
    public String getNote_status()
    {
        return this.note_status;
    }
    public void setLock_status(int paramString)
    {
        this.lock_status = paramString;
    }
    public void setNote_status(String paramString)
    {
        this.note_status = paramString;
    }

    public Long getId() {
        return id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public List<Note> getNote(){
        return notes;
    }

    public String getTag(){
        return tag;
    }

    public void setTag(String tag){
        this.tag = tag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    public void getBoolean(boolean imgbtn){
        this.imgbtn = imgbtn;
    }


    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (obj==null) return false;
        if (getClass()!=obj.getClass()) return false;
        Note note= (Note) obj;
        if (content==null){
            if (note.content!=null) return false;
        } else if (!content.equals(note.content)) return false;

        if (title==null){
            if (note.title!=null)return false;
        } else if (!title.equals(note.title)) return false;

        if (tag==null){
            if (note.tag!=null)return false;
        } else if (!tag.equals(note.tag)) return false;

        if (id==null){
            if (note.id!=null)return false;
        } else if (!id.equals(note.id)) return false;

        if (updatedAt==null){
            if (note.updatedAt!=null)return false;
        } else if (!updatedAt.equals(note.updatedAt)) return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Note [id=").append(id)
                .append(", title=").append(title)
                .append(", content").append(content)
                .append(",tag").append(tag)
                .append(", updatedAt").append(updatedAt)
                .append("]").toString();
    }

    @Override
    public int hashCode() {
        final int prime=31;
        int result=1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        return result;
    }
}
