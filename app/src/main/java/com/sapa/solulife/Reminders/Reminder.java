package com.sapa.solulife.Reminders;

import com.sapa.solulife.Notes.Note;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Pooja S on 10/1/2016.
 */

public class Reminder implements Serializable {
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

    private String reminderDate;
    private int reminderStatus;
    private String reminderTime;

    private byte[] image;

    private int favourite;

    public String getReminderDate()
    {
        return this.reminderDate;
    }

    public int getReminderStatus()
    {
        return this.reminderStatus;
    }

    public String getReminderTime()
    {
        return this.reminderTime;
    }

    public void setReminderDate(String reminderDate)
    {
        this.reminderDate = reminderDate;
    }

    public void setReminderStatus(int reminderStatus)
    {
        this.reminderStatus = reminderStatus;
    }

    public void setReminderTime(String reminderTime)
    {
        this.reminderTime = reminderTime;
    }

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
