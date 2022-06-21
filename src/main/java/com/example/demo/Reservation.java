/* Copyright 2022 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
*/

package com.example.demo;

import com.google.cloud.Timestamp;
import com.google.cloud.Date;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class Reservation {
  private String id;

  public String getId() {
    return id;
}
public void setId(String id) {
    this.id = id;
}
public String getAptId() {
    return aptId;
}
public void setAptId(String aptId) {
    this.aptId = aptId;
}
public int getHourNumber() {
    return hourNumber;
}
public void setHourNumber(int hourNumber) {
    this.hourNumber = hourNumber;
}
public int getPlayerCount() {
    return playerCount;
}
public void setPlayerCount(int playerCount) {
    this.playerCount = playerCount;
}

  public String aptId;

  public int hourNumber;

  public int playerCount;


}
