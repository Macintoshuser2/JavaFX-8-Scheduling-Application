package main.util;

import java.util.ArrayList;

public class BulletJournalEntry {
    private ArrayList<String> page;
    private ArrayList<String> pageContent;
    private ArrayList<String> keys;

    public ArrayList<String> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<String> keys) {
        this.keys = keys;
    }

    public BulletJournalEntry() {
        this.page = new ArrayList<String>();
        this.pageContent = new ArrayList<String>();
        this.keys = new ArrayList<String>();

    }

    public BulletJournalEntry(ArrayList<String> page, ArrayList<String> pageContent, ArrayList<String> keys) {
        this.page = page;
        this.pageContent = pageContent;
        this.keys = keys;
    }

    public ArrayList<String> getPage() {
        return page;
    }

    public void setPage(ArrayList<String> page) {
        this.page = page;
    }

    public ArrayList<String> getPageContent() {
        return pageContent;
    }

    public void setPageContent(ArrayList<String> pageContent) {
        this.pageContent = pageContent;
    }
}
