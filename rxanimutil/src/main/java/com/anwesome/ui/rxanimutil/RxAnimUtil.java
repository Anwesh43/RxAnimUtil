package com.anwesome.ui.rxanimutil;

import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
/**
 * Created by anweshmishra on 18/01/17.
 */
public class RxAnimUtil {
    private static List<Subscription> subscriptions = new ArrayList<>();
    public static void pause() {
        for(Subscription subscription:subscriptions) {
            if(!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }
    private static void animateView(int timeInMilliSeconds, final RxAnimationListener animationListener) {
       final int currentIndex = subscriptions.size();
       subscriptions.add(Observable.interval(timeInMilliSeconds, TimeUnit.MILLISECONDS).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.newThread()).subscribe(new Action1<Long>() {
           @Override
           public void call(Long aLong) {
               animationListener.animate();
               if(animationListener.checkStopCondition()) {
                   Subscription subscription = subscriptions.get(currentIndex);
                   if(!subscription.isUnsubscribed()) {
                       subscription.unsubscribe();
                   }
               }
           }
       }));
    }
    public static void rotateView(final View view,final int from,final int to,int timeInMilliSeconds){
        view.setRotation(from);
        animateView(timeInMilliSeconds,new RxAnimationListener(){
           public void animate() {
               view.setRotation(view.getRotation()+RxAnimConstants.ROT_SPEED);
           }
            public boolean checkStopCondition() {
                boolean condition =  view.getRotation() >= to;
                if(condition) {
                    view.setRotation(to);
                }
                return condition;
            }
        });
    }
    public static void scaleView(final View view,float from,final float to,int timeInMilliSeconds){
        view.setScaleX(from);
        view.setScaleY(from);
        final float dir = from!=to?(to-from)/Math.abs(to-from):0;
        animateView(timeInMilliSeconds, new RxAnimationListener() {
            @Override
            public void animate() {
                view.setScaleX(view.getScaleX()+dir*RxAnimConstants.SCALE_SPEED);
                view.setScaleY(view.getScaleY()+dir*RxAnimConstants.SCALE_SPEED);
            }

            @Override
            public boolean checkStopCondition() {
                if(dir>0) {
                    if(view.getScaleX()>=to && view.getScaleY()>=to) {
                        view.setScaleX(to);
                        view.setScaleY(to);
                        return true;
                    }
                }
                else {
                    if(view.getScaleX()<=to && view.getScaleY()<=to){
                        view.setScaleX(to);
                        view.setScaleY(to);
                        return true;
                    }
                }
                return false;
            }
        });
    }
    public static void translateView(final View view, final int from, final int to, final RxAnimTranslation rxAnimTranslation, int timeInMilliSeconds) {
        switch (rxAnimTranslation) {
            case X:
                view.setTranslationX(from);
                break;
            case Y:
                view.setTranslationY(from);
                break;
            case XY:
                view.setTranslationX(from);
                view.setTranslationY(from);
                break;
            default:
                break;
        }
        final int dir = (to!=from)?(to-from)/Math.abs(to-from):0;
        animateView(timeInMilliSeconds, new RxAnimationListener() {
            @Override
            public void animate() {

                switch (rxAnimTranslation) {
                    case X:
                        view.setTranslationX(view.getTranslationX()+dir*RxAnimConstants.TRANS_SPEED);
                        break;
                    case Y:
                        view.setTranslationY(view.getTranslationY()+dir*RxAnimConstants.TRANS_SPEED);
                        break;
                    case XY:
                        view.setTranslationX(view.getTranslationX()+dir*RxAnimConstants.TRANS_SPEED);
                        view.setTranslationY(view.getTranslationY()+dir*RxAnimConstants.TRANS_SPEED);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public boolean checkStopCondition() {
                boolean condition = false;
                switch(rxAnimTranslation) {
                    case X:
                        if(from>to) {
                            condition = view.getTranslationX()<=to;
                            view.setTranslationX(to);
                        }
                        else {
                            condition = view.getTranslationX()>=to;
                            view.setTranslationX(to);
                        }
                        break;
                    case Y:
                        if(from>to) {
                            condition = view.getTranslationY()<=to;
                            if(condition) {
                                view.setTranslationY(to);
                            }
                        }
                        else {
                            condition = view.getTranslationY()>=to;
                            if(condition) {
                                view.setTranslationY(to);
                            }
                        }
                        break;
                    case XY:
                        if(from>to) {
                            condition = view.getTranslationY()<=to && view.getTranslationX()<=to;
                            if(condition) {
                                view.setTranslationX(to);
                                view.setTranslationY(to);
                            }
                        }
                        else if(from < to){
                            condition = view.getTranslationX()>=to && view.getTranslationY()>=to;
                            if(condition) {
                                view.setTranslationX(to);
                                view.setTranslationY(to);
                            }
                        }
                        break;
                    default:
                        break;
                }

                return condition;
            }
        });
    }
    public enum RxAnimTranslation {
        Y,X,XY;
    }
    private interface RxAnimationListener {
        void animate();
        boolean checkStopCondition();
    }
}
