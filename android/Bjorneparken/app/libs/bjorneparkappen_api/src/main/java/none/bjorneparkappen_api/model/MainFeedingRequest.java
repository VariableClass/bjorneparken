/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2017-02-15 17:18:02 UTC)
 * on 2017-02-26 at 17:11:06 UTC 
 * Modify at your own risk.
 */

package none.bjorneparkappen_api.model;

/**
 * Model definition for MainFeedingRequest.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the bjorneparkappen_api. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MainFeedingRequest extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<MainInternationalMessage> description;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("end_time")
  private java.lang.String endTime;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("is_active")
  private java.lang.Boolean isActive;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("keeper_id") @com.google.api.client.json.JsonString
  private java.lang.Long keeperId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<MainInternationalMessage> label;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("location_id") @com.google.api.client.json.JsonString
  private java.lang.Long locationId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("start_time")
  private java.lang.String startTime;

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<MainInternationalMessage> getDescription() {
    return description;
  }

  /**
   * @param description description or {@code null} for none
   */
  public MainFeedingRequest setDescription(java.util.List<MainInternationalMessage> description) {
    this.description = description;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getEndTime() {
    return endTime;
  }

  /**
   * @param endTime endTime or {@code null} for none
   */
  public MainFeedingRequest setEndTime(java.lang.String endTime) {
    this.endTime = endTime;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getIsActive() {
    return isActive;
  }

  /**
   * @param isActive isActive or {@code null} for none
   */
  public MainFeedingRequest setIsActive(java.lang.Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getKeeperId() {
    return keeperId;
  }

  /**
   * @param keeperId keeperId or {@code null} for none
   */
  public MainFeedingRequest setKeeperId(java.lang.Long keeperId) {
    this.keeperId = keeperId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<MainInternationalMessage> getLabel() {
    return label;
  }

  /**
   * @param label label or {@code null} for none
   */
  public MainFeedingRequest setLabel(java.util.List<MainInternationalMessage> label) {
    this.label = label;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getLocationId() {
    return locationId;
  }

  /**
   * @param locationId locationId or {@code null} for none
   */
  public MainFeedingRequest setLocationId(java.lang.Long locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getStartTime() {
    return startTime;
  }

  /**
   * @param startTime startTime or {@code null} for none
   */
  public MainFeedingRequest setStartTime(java.lang.String startTime) {
    this.startTime = startTime;
    return this;
  }

  @Override
  public MainFeedingRequest set(String fieldName, Object value) {
    return (MainFeedingRequest) super.set(fieldName, value);
  }

  @Override
  public MainFeedingRequest clone() {
    return (MainFeedingRequest) super.clone();
  }

}
