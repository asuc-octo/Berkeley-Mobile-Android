package com.asuc.asucmobile.infrastructure;

import com.asuc.asucmobile.GlobalApplication;
import com.asuc.asucmobile.infrastructure.transformers.DiningHallTransformer;
import com.asuc.asucmobile.models.DiningHall;
import com.asuc.asucmobile.values.FirebaseCollectionNames;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Repository wrapping CRUD operations for DiningHalls around Firebase
 * TODO: probably make an interface around this later
 */
public class DiningHallRepository {

    @Inject
    FirebaseFirestore firestore;

    private DiningHallTransformer mTransformer;

    private CollectionReference mRef;

    @Inject
    @Singleton
    public DiningHallRepository() {
        GlobalApplication.getFirebaseComponent().inject(this);
        mTransformer = new DiningHallTransformer();
        mRef = firestore.collection(FirebaseCollectionNames.DINING_HALL);
    }

    /**
     * Scan all dining halls
     * @param diningHalls
     * @return
     */
    public List<DiningHall> scanAllDiningHalls(final List<DiningHall> diningHalls) {
        mRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    diningHalls.clear();
                    diningHalls.addAll(mTransformer.dingingHallQSDomainTransformer(queryDocumentSnapshots));
                }
            }
        });
        return diningHalls;
    }
}
