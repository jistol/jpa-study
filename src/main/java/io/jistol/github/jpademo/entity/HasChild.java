package io.jistol.github.jpademo.entity;

import java.util.List;

public interface HasChild<T> {
    List<T> getChildList();
    void setChildList(List<T> list);
}
