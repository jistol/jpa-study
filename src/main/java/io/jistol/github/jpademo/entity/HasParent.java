package io.jistol.github.jpademo.entity;

public interface HasParent<T> {
    T getParent();
    void setParent(T p);
}
