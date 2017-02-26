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
 * Model definition for MainVisitorRequest.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the bjorneparkappen_api. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MainVisitorRequest extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("visit_end")
  private com.google.api.client.util.DateTime visitEnd;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("visit_start")
  private com.google.api.client.util.DateTime visitStart;

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getVisitEnd() {
    return visitEnd;
  }

  /**
   * @param visitEnd visitEnd or {@code null} for none
   */
  public MainVisitorRequest setVisitEnd(com.google.api.client.util.DateTime visitEnd) {
    this.visitEnd = visitEnd;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getVisitStart() {
    return visitStart;
  }

  /**
   * @param visitStart visitStart or {@code null} for none
   */
  public MainVisitorRequest setVisitStart(com.google.api.client.util.DateTime visitStart) {
    this.visitStart = visitStart;
    return this;
  }

  @Override
  public MainVisitorRequest set(String fieldName, Object value) {
    return (MainVisitorRequest) super.set(fieldName, value);
  }

  @Override
  public MainVisitorRequest clone() {
    return (MainVisitorRequest) super.clone();
  }

}
