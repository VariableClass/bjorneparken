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
 * Model definition for MainAreaListResponse.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the bjorneparkappen_api. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MainAreaListResponse extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<MainAmenityResponse> amenities;

  static {
    // hack to force ProGuard to consider MainAmenityResponse used, since otherwise it would be stripped out
    // see https://github.com/google/google-api-java-client/issues/543
    com.google.api.client.util.Data.nullOf(MainAmenityResponse.class);
  }

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<MainEnclosureResponse> enclosures;

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<MainAmenityResponse> getAmenities() {
    return amenities;
  }

  /**
   * @param amenities amenities or {@code null} for none
   */
  public MainAreaListResponse setAmenities(java.util.List<MainAmenityResponse> amenities) {
    this.amenities = amenities;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<MainEnclosureResponse> getEnclosures() {
    return enclosures;
  }

  /**
   * @param enclosures enclosures or {@code null} for none
   */
  public MainAreaListResponse setEnclosures(java.util.List<MainEnclosureResponse> enclosures) {
    this.enclosures = enclosures;
    return this;
  }

  @Override
  public MainAreaListResponse set(String fieldName, Object value) {
    return (MainAreaListResponse) super.set(fieldName, value);
  }

  @Override
  public MainAreaListResponse clone() {
    return (MainAreaListResponse) super.clone();
  }

}