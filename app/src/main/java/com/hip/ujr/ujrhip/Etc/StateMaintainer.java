package com.hip.ujr.ujrhip.Etc;

/**
 * ---------------------------------------------------
 * Created by Tin Megali on 19/03/16.
 * Project: tuts+mvp_sample
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Retains and maintain object's state between configuration changes
 * in Activitys and Fragments.
 *
 * Created by Tin Megali on 24/02/16. <br>
 * Project: AndroidMVP <br>
 *
 * <a href="http://www.tinmegali.com">www.tinmegali.com</a>
 * Based on <a href="https://github.com/douglascraigschmidt/POSA-15/tree/master/ex/AcronymExpander/src/vandy/mooc">
 *     framework MVP</a> developed by
 * <a href="https://github.com/douglascraigschmidt">
 *     Dr. Douglas Schmidth</a>
 *
 * @see <a href="https://github.com/tinmegali/simple-mvp">Project's Git</a> <br>
 * @see <a href="https://github.com/tinmegali/simple-mvp/tree/master/AndroidMVP/app">Sample Application</a>
 * @see <a href="https://github.com/tinmegali/simple-mvp/blob/master/AndroidMVP/app/src/main/java/com/tinmegali/androidmvp/main/MVP_MainActivity.java">
 *         Sample MVP interface
 *     </a>
 */
public class StateMaintainer {

    private final String TAG = getClass().getSimpleName();

    private final String mStateMaintainerTag;
    private final WeakReference<FragmentManager> mFragmentManager;
    private StateMngFragment mStateMaintainerFrag;
    private boolean mIsRecreating;

    public StateMaintainer(FragmentManager fragmentManager, String stateMaintainerTAG) {
        mFragmentManager = new WeakReference<>(fragmentManager);
        mStateMaintainerTag = stateMaintainerTAG;
    }

    /**
     * 오브젝트 유지하는 프래그먼트 생성
     * @return  true: 프래그먼트 생성
     */
    public boolean firstTimeIn() {
        try {
            //프래그먼트 지정된 태그로 가져오기
            mStateMaintainerFrag = (StateMngFragment)mFragmentManager.get().findFragmentByTag(mStateMaintainerTag);

            // 처음 접근시 태그로 프래그먼트 설정
            if (mStateMaintainerFrag == null) {
                mStateMaintainerFrag = new StateMngFragment();
                mFragmentManager.get().beginTransaction().add(mStateMaintainerFrag, mStateMaintainerTag).commit();
                mIsRecreating = false;
                return true;
            } else {
                mIsRecreating = true;
                return false;
            }
        } catch (NullPointerException e) {
            Log.w(TAG, "Erro firstTimeIn()");
            return false;
        }
    }

    /**
     * 접근 2번이상인지 리턴(true 면 1번, false 면 2번이상)
     * @return  If the Activity was recreated
     */
    public boolean wasRecreated() { return mIsRecreating; }


    /**
     * 저장할 오브젝트 넣기
     * @param key   저장 키값
     * @param obj   저장할 오브젝트
     */
    public void put(String key, Object obj) {
        mStateMaintainerFrag.put(key, obj);
    }

    /**
     * 저장할 오브젝트 넣기
     * @param obj   저장할 오브젝트
     */
    public void put(Object obj) {
        put(obj.getClass().getName(), obj);
    }


    /**
     * 저장된 오브젝트 가져오기
     * @param key   키값
     * @param <T>   오브젝트 타입
     * @return      리턴 : 오브젝트
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key)  {
        return mStateMaintainerFrag.get(key);

    }

    /**
     * 해당 키값의 오브젝트 있는지 확인
     * @param key   키값
     * @return      유무 Bool
     */
    public boolean hasKey(String key) {
        return mStateMaintainerFrag.get(key) != null;
    }


    /**
     * 객체 보존용 프래그먼트
     * 해시맵 이용하여 obj 저장
     */
    public static class StateMngFragment extends Fragment {
        private HashMap<String, Object> mData = new HashMap<>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // 프래그먼트 안죽도록 (재시작 안되게)
            setRetainInstance(true);
        }

        /**
         * 해시맵에 오브젝트 저장
         * @param key   참조 키값
         * @param obj   저장할 오브젝트
         */
        public void put(String key, Object obj) {
            mData.put(key, obj);
        }

        /**
         * Insert objects on the hashmap
         * @param object    저장할 오브젝트
         */
        public void put(Object object) {
            put(object.getClass().getName(), object);
        }

        /**
         * 저장된 오브젝트 가져오기
         * @param key   참조값
         * @param <T>   오브젝트 타입
         * @return      저장된 오브젝트
         */
        @SuppressWarnings("unchecked")
        public <T> T get(String key) {
            return (T) mData.get(key);
        }
    }

}
