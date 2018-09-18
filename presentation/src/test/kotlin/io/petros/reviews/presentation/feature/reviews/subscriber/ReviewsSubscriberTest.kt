package io.petros.reviews.presentation.feature.reviews.subscriber

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.petros.reviews.domain.model.review.ReviewsResultPage
import io.petros.reviews.presentation.feature.common.list.adapter.AdapterStatus
import io.petros.reviews.test.domain.TestReviewsProvider.Companion.provideReviewsResultPage
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReviewsSubscriberTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val reviewsResultPage = provideReviewsResultPage()

    private lateinit var testedClass: ReviewsSubscriber
    private val statusObservableMock = mock<Observer<AdapterStatus>>()
    private val reviewsResultPageObservableMock = mock<Observer<ReviewsResultPage>>()

    @Before
    fun setUp() {
        testedClass = ReviewsSubscriber(MutableLiveData(), MutableLiveData())
        testedClass.statusObservable.observeForever(statusObservableMock)
        testedClass.reviewsObservable.observeForever(reviewsResultPageObservableMock)
    }

    @Test
    fun `When load reviews succeeds, then an idle status is posted`() {
        testedClass.onSuccess(reviewsResultPage)

        verify(statusObservableMock).onChanged(AdapterStatus.IDLE)
    }

    @Test
    fun `When load reviews succeeds, then the reviews result page is posted`() {
        testedClass.onSuccess(reviewsResultPage)

        verify(reviewsResultPageObservableMock).onChanged(reviewsResultPage)
    }

    @Test
    fun `When load reviews fails, then an error status is posted`() {
        testedClass.onError(Exception())

        verify(statusObservableMock).onChanged(AdapterStatus.ERROR)
    }

    @Test
    fun `When load reviews fails, then a null reviews result page is posted`() {
        testedClass.onError(Exception())

        verify(reviewsResultPageObservableMock).onChanged(null)
    }

}