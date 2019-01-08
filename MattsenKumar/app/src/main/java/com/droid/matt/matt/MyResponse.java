package com.droid.matt.matt;

import java.util.List;

public class MyResponse {

    public List<Images> getSponsors() {
        return sponsors;
    }

    public void setSponsors(List<Images> sponsors) {
        this.sponsors = sponsors;
    }

    private List<Images> sponsors;
}
