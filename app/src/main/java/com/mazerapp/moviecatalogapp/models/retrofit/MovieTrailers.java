package com.mazerapp.moviecatalogapp.models.retrofit;

import java.util.List;

public class MovieTrailers {

    private int id;
    private List<TrailerInfo> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TrailerInfo> getResults() {
        return results;
    }

    public void setResults(List<TrailerInfo> results) {
        this.results = results;
    }

    public class TrailerInfo{
        private String id;
        private String key;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
