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
    private interface RxAnimationListener {
        void animate();
        boolean checkStopCondition();
    }
}
