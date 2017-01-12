package edu.buaa.bwc.buaa_check.POJOs;

import java.util.List;

/**
 * Created by XJX on 2017/1/3.
 */

public class ListResponse<T> {
    public int total;
    public List<T> rows;
}
