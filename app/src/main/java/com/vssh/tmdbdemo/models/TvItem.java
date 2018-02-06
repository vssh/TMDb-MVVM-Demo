package com.vssh.tmdbdemo.models;

import java.util.List;

/**
 * Semi-generated Bean-style class corresponding to a single TV result from TMDb.
 */

public class TvItem extends GenericItem {
    /**
     * poster_path : /esN3gWb1P091xExLddD2nh4zmi3.jpg
     * popularity : 37.882356
     * id : 62560
     * backdrop_path : /v8Y9yurHuI7MujWQMd8iL3Gy4B5.jpg
     * vote_average : 7.5
     * overview : A contemporary and culturally resonant drama about a young programmer, Elliot, who suffers from a debilitating anti-social disorder and decides that he can only connect to people by hacking them. He wields his skills as a weapon to protect the people that he cares about. Elliot will find himself in the intersection between a cybersecurity firm he works for and the underworld organizations that are recruiting him to bring down corporate America.
     * first_air_date : 2015-05-27
     * origin_country : ["US"]
     * genre_ids : [80,18]
     * original_language : en
     * vote_count : 287
     * name : Mr. Robot
     * original_name : Mr. Robot
     */


    private String first_air_date;
    private String name;
    private String original_name;
    private List<String> origin_country;

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public List<String> getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(List<String> origin_country) {
        this.origin_country = origin_country;
    }
}
