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
 * Model definition for MainEventResponse.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the bjorneparkappen_api. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MainEventResponse extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String description;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("end_time")
  private java.lang.String endTime;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("is_active")
  private java.lang.Boolean isActive;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String label;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private MainAmenityResponse location;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("start_time")
  private java.lang.String startTime;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDescription() {
    return description;
  }

  /**
   * @param description description or {@code null} for none
   */
  public MainEventResponse setDescription(java.lang.String description) {
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
  public MainEventResponse setEndTime(java.lang.String endTime) {
    this.endTime = endTime;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public MainEventResponse setId(java.lang.Long id) {
    this.id = id;
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
  public MainEventResponse setIsActive(java.lang.Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLabel() {
    return label;
  }

  /**
   * @param label label or {@code null} for none
   */
  public MainEventResponse setLabel(java.lang.String label) {
    this.label = label;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public MainAmenityResponse getLocation() {
    return location;
  }

  /**
   * @param location location or {@code null} for none
   */
  public MainEventResponse setLocation(MainAmenityResponse location) {
    this.location = location;
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
  public MainEventResponse setStartTime(java.lang.String startTime) {
    this.startTime = startTime;
    return this;
  }

  @Override
  public MainEventResponse set(String fieldName, Object value) {
    return (MainEventResponse) super.set(fieldName, value);
  }

  @Override
  public MainEventResponse clone() {
    return (MainEventResponse) super.clone();
  }

}
