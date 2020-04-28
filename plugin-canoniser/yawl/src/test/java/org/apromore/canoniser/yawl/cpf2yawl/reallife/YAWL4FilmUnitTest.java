/*-
 * #%L
 * This file is part of "Apromore Community".
 *
 * Copyright (C) 2012 - 2017 Queensland University of Technology.
 * Copyright (C) 2012 Felix Mannhardt.
 * %%
 * Copyright (C) 2018 - 2020 The University of Melbourne.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

package org.apromore.canoniser.yawl.cpf2yawl.reallife;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.apromore.canoniser.yawl.BaseCPF2YAWLUnitTest;
import org.apromore.canoniser.yawl.utils.TestUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.yawlfoundation.yawlschema.ExternalTaskFactsType;
import org.yawlfoundation.yawlschema.NetFactsType;

public class YAWL4FilmUnitTest extends BaseCPF2YAWLUnitTest {

    /*
     * (non-Javadoc)
     *
     * @see org.apromore.canoniser.yawl.BaseCPF2YAWLUnitTest#getCPFFile()
     */
    @Override
    protected File getCPFFile() {
        return new File(TestUtils.TEST_RESOURCES_DIRECTORY + "CPF/Internal/FromYAWL/filmproduction.yawl.cpf");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apromore.canoniser.yawl.BaseCPF2YAWLUnitTest#getANFFile()
     */
    @Override
    protected File getANFFile() {
        return new File(TestUtils.TEST_RESOURCES_DIRECTORY + "CPF/Internal/FromYAWL/filmproduction.yawl.anf");
    }

    @Test
    public void testNetData() {
        NetFactsType net = findRootNet();
        checkOutputParameter("finalCallSheetSubmission", "boolean", net);
        checkOutputParameter("timeSheetInfo", "timeSheetInfoType", net);
        checkOutputParameter("soundInfo", "soundInfoType", net);
        checkOutputParameter("DPRinfo", "DPRinfoType", net);
        checkLocalVariable("shortEndMin", "long", net);
        checkLocalVariable("prevShot", "scriptTimingType", net);
    }

    @Ignore
    @Test
    public void testTaskMappings() {
        NetFactsType net = findRootNet();
        ExternalTaskFactsType taskFact1 = findTaskByName("Create Call Sheet", net);
        assertNotNull(taskFact1);
        checkInputMapping("callSheet", " {/Film_Production_Process/productionOffice}  {for $x in /Film_Production_Process/shootingSchedule/singleDaySchedule   where $x/shootDayNo=/Film_Production_Process/shootDayNoNextday/text() return  <emergencyInfo>   <fireAmbulance>000</fireAmbulance>   <hospital>   {if (for $y in /Film_Production_Process/locationInfo/singleLocationInfo         where $y/locationID=$x/sceneSchedule/locationID return true())    then (for $y in /Film_Production_Process/locationInfo/singleLocationInfo           where $y/locationID=$x/sceneSchedule/locationID return data($y/hospital))    else 'Missing info...'}   </hospital>   <police>   {if (for $y in /Film_Production_Process/locationInfo/singleLocationInfo         where $y/locationID=$x/sceneSchedule/locationID return true())    then (for $y in /Film_Production_Process/locationInfo/singleLocationInfo           where $y/locationID=$x/sceneSchedule/locationID return data($y/police))    else 'Missing info...'}   </police>  </emergencyInfo>}  {/Film_Production_Process/crewMember/director}  {/Film_Production_Process/crewMember/producer}  {for $x in /Film_Production_Process/crewInfo/singleCrewInfo   where contains($x/role,'Production Manager') return  <productionManager>   {concat($x/firstName,' ',$x/lastName,' ',$x/contactNo)}  </productionManager>}  <firstAD>   {for $x in /Film_Production_Process/crewInfo/singleCrewInfo    where $x/role='1st AD' return     concat($x/firstName,' ',$x/lastName,' ',$x/contactNo)}  </firstAD>  {/Film_Production_Process/sunrise}  {/Film_Production_Process/sunset}  {/Film_Production_Process/weather}   {for $x in /Film_Production_Process/shootingSchedule/singleDaySchedule   where $x/shootDayNo=/Film_Production_Process/shootDayNoNextday/text() return  <callTimes>   <crewCall><callTime>{$x/crewCall/text()}</callTime></crewCall>   <locationCall><callTime>{$x/travelToLoc/text()}</callTime></locationCall>   <makeupHair><callTime>00:00:00</callTime></makeupHair>   <wardrobe><callTime>00:00:00</callTime></wardrobe>   <unit><callTime>00:00:00</callTime></unit>   <breakfast><from>00:00:00</from><to>00:00:00</to></breakfast>  </callTimes>}  <wrapTimes>  <estWrap>Estimate the wrap time...</estWrap>  </wrapTimes>  <location>   {for $x in /Film_Production_Process/shootingSchedule/singleDaySchedule   where $x/shootDayNo=/Film_Production_Process/shootDayNoNextday/text()   return    (if (for $y in /Film_Production_Process/locationInfo/singleLocationInfo     where $y/locationID=$x/sceneSchedule/locationID return true())    then (for $y in /Film_Production_Process/locationInfo/singleLocationInfo     where $y/locationID=$x/sceneSchedule/locationID return   <singleLocation>    {$y/locationName}    {$y/address}    {$y/contact}    {$y/contactNo}    {$y/UBDMapRef}    {$y/locationNotes}   </singleLocation>)    else    <singleLocation>    <locationName>Missing Info. Please Check ...</locationName>    <address>n/a</address>    <contact>n/a</contact>    <contactNo>0</contactNo>    <UBDMapRef>0</UBDMapRef>    <locationNotes>n/a</locationNotes>   </singleLocation>)}  </location>   {for $x in /Film_Production_Process/shootingSchedule/singleDaySchedule    where $x/shootDayNo=/Film_Production_Process/shootDayNoNextday/text() return   <dailySchedule>   <startDayNotes>   {if (contains($x/startDayNotes,'')) then $x/startDayNotes/text() else 'nil'}</startDayNotes>   {for $y in $x/sceneSchedule return   <sceneSchedule>    {$y/scene}    {$y/pageTime}    {$y/estScriptTiming}    {$y/D_N}    {$y/IN_EX}    <setLocation>    {if (for $z in /Film_Production_Process/locationInfo/singleLocationInfo      where $z/locationID=$y/locationID return true())     then (for $z in /Film_Production_Process/locationInfo/singleLocationInfo      where $z/locationID=$y/locationID return concat($y/set,' @ ',$z/locationName))     else 'Missing info. Please check ...'}    </setLocation>    {$y/synopsis}    {for $z in $y/characters/character return    <artistTimeInfo>     {$z}     {if (for $w in /Film_Production_Process/castInfo/singleCastInfo      where $w/character=$z return true())      then (for $w in /Film_Production_Process/castInfo/singleCastInfo      where $w/character=$z return $w/artist)      else <artist>Missing info. Please Check ...</artist>}     <pickup>-</pickup>     <makeup>-</makeup>     <wardrobe>-</wardrobe>     <onSet>-</onSet>    </artistTimeInfo>}    {$y/estShootTimes}    <sceneNotes>-</sceneNotes>    {$y/mealBreak}      </sceneSchedule>}   {$x/endDayNotes}   {$x/totalScriptPages}  </dailySchedule>}   {for $x in /Film_Production_Process/shootingSchedule/singleDaySchedule    where $x/shootDayNo=/Film_Production_Process/shootDayNoNextday/text()   return   <dailySetRequirements>   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Background Actors' return   <singleEntry>    <item>Background Actors</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Background Actors' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Stunts' return   <singleEntry>    <item>Stunts</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Stunts' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Vehicles' return   <singleEntry>    <item>Vehicles</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Vehicles' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Props' return   <singleEntry>    <item>Props</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Props' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Camera' return   <singleEntry>    <item>Camera</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Camera' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Special Effects' return   <singleEntry>    <item>Special Effects</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Special Effects' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Wardrobe' return   <singleEntry>    <item>Wardrobe</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Wardrobe' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Makeup/Hair' return   <singleEntry>    <item>Makeup/Hair</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Makeup/Hair' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Animal' return   <singleEntry>    <item>Animal</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Animal' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Animal Wrangler' return   <singleEntry>    <item>Animal Wrangler</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Animal Wrangler' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Music' return   <singleEntry>    <item>Music</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Music' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Sound' return   <singleEntry>    <item>Sound</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Sound' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Art Department' return   <singleEntry>    <item>Art Department</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Art Department' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Set Dressing' return   <singleEntry>    <item>Set Dressing</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Set Dressing' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Greenery' return   <singleEntry>    <item>Greenery</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Greenery' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Special Equipment' return   <singleEntry>    <item>Special Equipment</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Special Equipment' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Security' return   <singleEntry>    <item>Security</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Security' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Additional Labor' return   <singleEntry>    <item>Additional Labor</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Additional Labor' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Visual Effects' return   <singleEntry>    <item>Visual Effects</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Visual Effects' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Mechanical Effects' return   <singleEntry>    <item>Mechanical Effects</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Mechanical Effects' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Miscellaneous' return   <singleEntry>    <item>Miscellaneous</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Miscellaneous' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}   {let $y:=$x/sceneSchedule/setRequirements/singleEntry    where $y/item='Notes' return   <singleEntry>    <item>Notes</item>    {for $w in $x/sceneSchedule, $z in $w/setRequirements/singleEntry     where $z/item='Notes' return    <description>     {$w/scene}     {$z/requirements}    </description>}   </singleEntry>}  </dailySetRequirements>}  <unit>   {for $x in /Film_Production_Process/shootingSchedule/singleDaySchedule   where $x/shootDayNo=/Film_Production_Process/shootDayNoNextday/text()   return   (if (for $y in /Film_Production_Process/locationInfo/singleLocationInfo    where $y/locationID=$x/sceneSchedule/locationID return true())    then (for $y in /Film_Production_Process/locationInfo/singleLocationInfo    where $y/locationID=$x/sceneSchedule/locationID    return concat($y/locationID,': ',$y/unit,'.'))    else 'Missing info...')}  </unit>  {/Film_Production_Process/callSheetNotes/additionalEquipment}  {/Film_Production_Process/callSheetNotes/additionalCrew}   {/Film_Production_Process/callSheetNotes/directions}  <parking>   {for $x in /Film_Production_Process/shootingSchedule/singleDaySchedule   where $x/shootDayNo=/Film_Production_Process/shootDayNoNextday/text()   return    (if (for $y in /Film_Production_Process/locationInfo/singleLocationInfo     where $y/locationID=$x/sceneSchedule/locationID return true())    then (for $y in /Film_Production_Process/locationInfo/singleLocationInfo     where $y/locationID=$x/sceneSchedule/locationID    return concat($y/locationID,': ',$y/parking,'.'))    else 'Missing info...')}  </parking>   {for $x in /Film_Production_Process/shootingSchedule/singleDaySchedule   where $x/shootDayNo=/Film_Production_Process/shootDayNoNextday/text()   return  <catering>   <singleMeal>    <meal>breakfast</meal>    <break>0000-0000</break>    <serveNo>0</serveNo>    <location>...</location>   </singleMeal>   {for $z in $x/sceneSchedule/mealBreak return   <singleMeal>    {$z/meal}    {$z/break}    <serveNo>0</serveNo>    <location>...</location>   </singleMeal>}  </catering>}  {/Film_Production_Process/callSheetNotes/productionNotes}  {/Film_Production_Process/callSheetNotes/locationCrewNotes}  {/Film_Production_Process/callSheetNotes/lunchPickup}  {/Film_Production_Process/callSheetNotes/exposedRushes}  {/Film_Production_Process/callSheetNotes/rushesScreening}  {/Film_Production_Process/callSheetNotes/crewAgreements}  {for $x in /Film_Production_Process/shootingSchedule/singleDaySchedule   where $x/shootDayNo=number(/Film_Production_Process/shootDayNoNextday/text())+1   return  <advancedSchedule>   {$x/shootDayNo}    {$x/shootDayDate}    {$x/shootDayWeekday}   {for $z in $x/sceneSchedule return   <adSceneSchedule>     {$z/scene}    {$z/pageTime}    {$z/D_N}    {$z/IN_EX}    <setSynopsis>{concat($z/set,' / ',$z/synopsis)}</setSynopsis>    <location>{$z/locationID/text()}</location>    {$z/characters}   </adSceneSchedule>}   </advancedSchedule>}  {/Film_Production_Process/callSheetNotes/additionalNotes}", taskFact1);

        ExternalTaskFactsType taskFact2 = findTaskByName("Fill Out Camera Sheets", net);
        assertNotNull(taskFact2);
        checkInputMapping("generalInfo", "{/Film_Production_Process/generalInfoToday/*}", taskFact2);
        checkInputMapping("producer", "{/Film_Production_Process/crewMember/producer/text()}", taskFact2);
        checkOutputMapping("stockInfoToday", "<loaded>{number(/Fill_Out_Camera_Sheets/stockInfo/loaded/text())+  number(/Fill_Out_Camera_Sheets/cameraInfo/camInfoSum/footageLoaded/text())}</loaded>"
  +"<gross>{number(/Fill_Out_Camera_Sheets/stockInfo/gross/text())+  number(/Fill_Out_Camera_Sheets/cameraInfo/camInfoSum/totalExposed/text())+  number(/Fill_Out_Camera_Sheets/cameraInfo/camInfoSum/waste/text())}</gross>"
  +"<exposed>{number(/Fill_Out_Camera_Sheets/stockInfo/exposed/text())+  number(/Fill_Out_Camera_Sheets/cameraInfo/camInfoSum/totalExposed/text())}</exposed>"
  +"<print>{number(/Fill_Out_Camera_Sheets/stockInfo/print/text())+  number(/Fill_Out_Camera_Sheets/cameraInfo/camInfoSum/totalExposed/text())}</print>"
  +"<N_G>{/Fill_Out_Camera_Sheets/stockInfo/N_G/text()}</N_G>"
  +"<waste>{number(/Fill_Out_Camera_Sheets/stockInfo/waste/text())+  number(/Fill_Out_Camera_Sheets/cameraInfo/camInfoSum/waste/text())}</waste>"
  +"<shortEnds>{number(/Fill_Out_Camera_Sheets/stockInfo/shortEnds/text())+  number(/Fill_Out_Camera_Sheets/cameraInfo/camInfoSum/shortEnds/text())}</shortEnds>", taskFact2);

    }

}
