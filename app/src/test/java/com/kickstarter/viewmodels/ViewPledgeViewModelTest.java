package com.kickstarter.viewmodels;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.kickstarter.KSRobolectricTestCase;
import com.kickstarter.factories.BackingFactory;
import com.kickstarter.factories.LocationFactory;
import com.kickstarter.factories.RewardFactory;
import com.kickstarter.factories.UserFactory;
import com.kickstarter.libs.Environment;
import com.kickstarter.libs.MockCurrentUser;
import com.kickstarter.libs.utils.DateTimeUtils;
import com.kickstarter.libs.utils.NumberUtils;
import com.kickstarter.models.Backing;
import com.kickstarter.models.Location;
import com.kickstarter.models.Project;
import com.kickstarter.models.Reward;
import com.kickstarter.models.RewardsItem;
import com.kickstarter.models.User;
import com.kickstarter.services.ApiClientType;
import com.kickstarter.services.MockApiClient;
import com.kickstarter.ui.IntentKey;

import org.junit.Test;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static java.util.Collections.emptyList;

public final class ViewPledgeViewModelTest extends KSRobolectricTestCase {
  private ViewPledgeViewModel vm;
  private final TestSubscriber<String> backerNameTextViewTextTest = TestSubscriber.create();
  private final TestSubscriber<String> backerNumberTextViewTextTest = TestSubscriber.create();

  private final TestSubscriber<Pair<String, String>> backingAmountAndDateTextViewTextTest = TestSubscriber.create();
  private final TestSubscriber<String> backingAmountTextViewTextTest = TestSubscriber.create();
  private final TestSubscriber<String> backingDateTextViewTextTest = TestSubscriber.create();

  private final TestSubscriber<String> backingStatusTextViewTest = TestSubscriber.create();
  private final TestSubscriber<String> creatorNameTextViewTextTest = TestSubscriber.create();
  private final TestSubscriber<Void> goBackTest = TestSubscriber.create();
  private final TestSubscriber<String> loadBackerAvatarTest = TestSubscriber.create();
  private final TestSubscriber<String> loadProjectPhotoTest = TestSubscriber.create();
  private final TestSubscriber<String> shippingLocationTextViewTextTest = TestSubscriber.create();
  private final TestSubscriber<String> shippingAmountTextViewTextTest = TestSubscriber.create();
  private final TestSubscriber<Boolean> shippingSectionIsHiddenTest = TestSubscriber.create();
  private final TestSubscriber<String> projectNameTextViewTextTest = TestSubscriber.create();
  private final TestSubscriber<Pair<String, String>> rewardMinimumAndDescriptionTextViewTextTest = TestSubscriber.create();
  private final TestSubscriber<List<RewardsItem>> rewardsItemsTest = TestSubscriber.create();
  private final TestSubscriber<Boolean> rewardsItemsAreHiddenTest = TestSubscriber.create();

  private void setUpEnvironment(final @NonNull Environment environment) {
    this.vm = new ViewPledgeViewModel(environment);

    this.vm.outputs.backerNameTextViewText().subscribe(this.backerNameTextViewTextTest);
    this.vm.outputs.backerNumberTextViewText().subscribe(this.backerNumberTextViewTextTest);

    this.vm.outputs.backingAmountAndDateTextViewText().subscribe(this.backingAmountAndDateTextViewTextTest);
    this.vm.outputs.backingAmountAndDateTextViewText().map(p -> p.first).subscribe(this.backingAmountTextViewTextTest);
    this.vm.outputs.backingAmountAndDateTextViewText().map(p -> p.second).subscribe(this.backingDateTextViewTextTest);

    this.vm.outputs.backingStatus().subscribe(this.backingStatusTextViewTest);
    this.vm.outputs.creatorNameTextViewText().subscribe(this.creatorNameTextViewTextTest);
    this.vm.outputs.goBack().subscribe(this.goBackTest);
    this.vm.outputs.loadBackerAvatar().subscribe(this.loadBackerAvatarTest);
    this.vm.outputs.loadProjectPhoto().subscribe(this.loadProjectPhotoTest);
    this.vm.outputs.shippingLocationTextViewText().subscribe(this.shippingLocationTextViewTextTest);
    this.vm.outputs.shippingAmountTextViewText().subscribe(this.shippingAmountTextViewTextTest);
    this.vm.outputs.shippingSectionIsHidden().subscribe(this.shippingSectionIsHiddenTest);
    this.vm.outputs.projectNameTextViewText().subscribe(this.projectNameTextViewTextTest);
    this.vm.outputs.rewardMinimumAndDescriptionTextViewText().subscribe(this.rewardMinimumAndDescriptionTextViewTextTest);
    this.vm.outputs.rewardsItems().subscribe(this.rewardsItemsTest);
    this.vm.outputs.rewardsItemsAreHidden().subscribe(this.rewardsItemsAreHiddenTest);
  }

  @Test
  public void testBackerNameTextViewText() {
    final Backing template = BackingFactory.backing();
    final Backing backing = template
      .toBuilder()
      .backer(
        template.backer()
          .toBuilder()
          .name("Blobby")
          .build()
      )
      .build();
    this.setUpEnvironment(environment(backing));

    this.vm.intent(this.intent(backing));

    this.backerNameTextViewTextTest.assertValues("Blobby");
  }

  @Test
  public void testBackerNumberTextViewText() {
    final Backing backing = BackingFactory.backing();
    this.setUpEnvironment(environment(backing));

    this.vm.intent(intent(backing));

    this.backerNumberTextViewTextTest.assertValues(NumberUtils.format(backing.sequence()));
  }

  @Test
  public void testBackingAmountAndDateTextViewText() {
    final Backing backing = BackingFactory.backing().toBuilder()
      .amount(50.0f)
      .build();
    this.setUpEnvironment(this.environment(backing));

    this.vm.intent(this.intent(backing));

    this.backingAmountTextViewTextTest.assertValues("$50");
    this.backingDateTextViewTextTest.assertValues(DateTimeUtils.fullDate(backing.pledgedAt()));
  }

  @Test
  public void testBackingStatus() {
    final Backing backing = BackingFactory.backing()
      .toBuilder()
      .status(Backing.STATUS_PLEDGED)
      .build();
    this.setUpEnvironment(environment(backing));

    this.vm.intent(this.intent(backing));

    this.backingStatusTextViewTest.assertValues(Backing.STATUS_PLEDGED);
  }

  @Test
  public void testCreatorNameTextViewText() {
    final Backing template = BackingFactory.backing();
    final Backing backing = template
      .toBuilder()
      .project(
        template.project()
          .toBuilder()
          .creator(
            template.project().creator()
              .toBuilder()
              .name("Blobby McBlob")
              .build()
          )
          .build()
      )
      .build();
    this.setUpEnvironment(environment(backing));

    this.vm.intent(this.intent(backing));

    this.creatorNameTextViewTextTest.assertValues("Blobby McBlob");
  }

  @Test
  public void testGoBackOnProjectClick() {
    final Backing backing = BackingFactory.backing();
    this.setUpEnvironment(environment(backing));

    this.vm.intent(this.intent(backing));
    this.goBackTest.assertNoValues();

    this.vm.inputs.projectClicked();
    this.goBackTest.assertValueCount(1);
  }

  @Test
  public void testLoadBackerAvatar() {
    final Backing template = BackingFactory.backing();
    final Backing backing = template
      .toBuilder()
      .backer(
        template.backer()
          .toBuilder()
          .avatar(
            template.backer().avatar()
              .toBuilder()
              .medium("http://www.kickstarter.com/med.jpg")
              .build()
          )
          .build()
      )
      .build();
    this.setUpEnvironment(environment(backing));

    this.vm.intent(this.intent(backing));

    this.loadBackerAvatarTest.assertValues("http://www.kickstarter.com/med.jpg");
  }

  @Test
  public void testLoadProjectPhoto() {
    final Backing template = BackingFactory.backing();
    final Backing backing = template
      .toBuilder()
      .project(
        template.project()
          .toBuilder()
          .photo(
            template.project().photo()
              .toBuilder()
              .full("http://www.kickstarter.com/full.jpg")
              .build()
          )
          .build()
      )
      .build();
    this.setUpEnvironment(environment(backing));

    this.vm.intent(this.intent(backing));

    this.loadProjectPhotoTest.assertValues("http://www.kickstarter.com/full.jpg");
  }

  @Test
  public void testProjectNameTextViewText() {
    final Backing template = BackingFactory.backing();
    final Backing backing = template
      .toBuilder()
      .project(
        template.project()
          .toBuilder()
          .name("A cool project")
          .build()
      )
      .build();
    this.setUpEnvironment(environment(backing));

    this.vm.intent(this.intent(backing));

    this.projectNameTextViewTextTest.assertValues("A cool project");
  }

  @Test
  public void testRewardMinimumAndDescriptionTextViewText() {
    final Reward reward = RewardFactory.reward()
      .toBuilder()
      .minimum(100.0f)
      .build();
    final Backing backing = BackingFactory.backing()
      .toBuilder()
      .reward(reward)
      .build();

    this.setUpEnvironment(this.environment(backing));

    this.vm.intent(this.intent(backing));

    this.rewardMinimumAndDescriptionTextViewTextTest.assertValues(Pair.create("$100", backing.reward().description()));
  }

  @Test
  public void testRewardsItemAreHidden() {
    final Reward reward = RewardFactory.reward().toBuilder()
      .rewardsItems(null)
      .build();
    final Backing backing = BackingFactory.backing().toBuilder()
      .reward(reward)
      .build();
    this.setUpEnvironment(this.environment(backing));

    this.vm.intent(this.intent(backing));

    this.rewardsItemsTest.assertValues(emptyList());
    this.rewardsItemsAreHiddenTest.assertValues(true);
  }

  @Test
  public void testRewardsItemAreEmitted() {
    final Reward reward = RewardFactory.itemized();
    final Backing backing = BackingFactory.backing().toBuilder()
      .reward(reward)
      .build();
    this.setUpEnvironment(this.environment(backing));

    this.vm.intent(this.intent(backing));

    this.rewardsItemsTest.assertValues(reward.rewardsItems());
    this.rewardsItemsAreHiddenTest.assertValues(false);
  }

  @Test
  public void testShipping_withoutShippingLocation() {
    final Backing backing = BackingFactory.backing();

    this.setUpEnvironment(this.environment(backing));

    this.vm.intent(this.intent(backing));

    this.shippingLocationTextViewTextTest.assertNoValues();
    this.shippingAmountTextViewTextTest.assertNoValues();
    this.shippingSectionIsHiddenTest.assertValues(true);
  }

  @Test
  public void testShipping_withShippingLocation() {
    final Location location = LocationFactory.sydney();
    final Reward reward = RewardFactory.rewardWithShipping();
    final Backing backing = BackingFactory.backing().toBuilder()
      .location(location)
      .reward(reward)
      .rewardId(reward.id())
      .shippingAmount(5.0f)
      .build();
    this.setUpEnvironment(this.environment(backing));

    this.vm.intent(this.intent(backing));


    this.shippingLocationTextViewTextTest.assertValues("Sydney, AU");
    this.shippingAmountTextViewTextTest.assertValues("$5");
    this.shippingSectionIsHiddenTest.assertValues(false);
  }

  private @NonNull ApiClientType apiClient(final @NonNull Backing backing) {
    return new MockApiClient() {
      @Override
      public @NonNull Observable<Backing> fetchProjectBacking(final @NonNull Project project, final @NonNull User user) {
        return Observable.just(backing);
      }
    };
  }

  private @NonNull Environment environment(final @NonNull Backing backing) {
    return environment().toBuilder()
      .apiClient(apiClient(backing))
      .currentUser(new MockCurrentUser(backing.backer()))
      .build();
  }

  private @NonNull Intent intent(final @NonNull Backing backing) {
    return new Intent().putExtra(IntentKey.PROJECT, backing.project());
  }
}
