/*-
 * #%L
 * This file is part of "Apromore Community".
 * 
 * Copyright (C) 2012 - 2017 Queensland University of Technology.
 * Copyright (C) 2012 Felix Mannhardt.
 * %%
 * Copyright (C) 2018 - 2020 Apromore Pty Ltd.
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

package org.apromore.canoniser.pnml.internal.canonical2pnml;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apromore.cpf.NodeType;
import org.apromore.pnml.ArcNameType;
import org.apromore.pnml.ArcToolspecificType;
import org.apromore.pnml.ArcType;
import org.apromore.pnml.GraphicsArcType;
import org.apromore.pnml.NodeNameType;
import org.apromore.pnml.OperatorType;
import org.apromore.pnml.PlaceToolspecificType;
import org.apromore.pnml.PlaceType;
import org.apromore.pnml.PositionType;
import org.apromore.pnml.TransitionToolspecificType;
import org.apromore.pnml.TransitionType;

public class TranslateANDJoinXORSplit {
    DataHandler data;
    long ids;

    public void setValues(DataHandler data, long ids) {
        this.data = data;
        this.ids = ids;
    }

    public void translate(NodeType node) {
        TransitionType tran = new TransitionType();
        TransitionToolspecificType trantool = new TransitionToolspecificType();
        OperatorType op = new OperatorType();
        NodeNameType test = null;
        if (node.getName() != null) {
            test = new NodeNameType();
            test.setText(node.getName());
        }
        data.put_id_map(node.getId(), String.valueOf(ids));
        tran.setId(String.valueOf(ids++));
        tran.setName(test);
        trantool.setTool("WoPeD");
        trantool.setVersion("1.0");
        String splitid[] = node.getOriginalID().split("_");
        op.setId(splitid[0]);
        op.setType(108);
        trantool.setOrientation(3);
        trantool.setOperator(op);
        tran.getToolspecific().add(trantool);
        PlaceType pcenter = new PlaceType();
        pcenter.setId("CENTER_PLACE_" + node.getName());
        pcenter.setName(test);
        PlaceToolspecificType ptt = new PlaceToolspecificType();
        ptt.setTool("WoPeD");
        ptt.setVersion("1.0");
        ptt.setOperator(op);
        pcenter.getToolspecific().add(ptt);
        ArcType joinarc = new ArcType();
        joinarc.setId("a950");
        joinarc.setSource(tran);
        joinarc.setTarget(pcenter);
        ArcNameType inscription = new ArcNameType();
        inscription.setText(1);
        joinarc.setInscription(inscription);
        joinarc.setGraphics(new GraphicsArcType());
        ArcToolspecificType at = new ArcToolspecificType();
        at.setTool("WoPeD");
        at.setDisplayProbabilityOn(false);
        at.setVersion("1.0");
        PositionType pt = new PositionType();
        pt.setX(BigDecimal.valueOf(Double.valueOf(500.0)));
        pt.setY(BigDecimal.valueOf(Double.valueOf(0.0)));
        at.setDisplayProbabilityPosition(pt);
        at.setProbability(1.0);
        joinarc.getToolspecific().add(at);
        data.getNet().getTransition().add(tran);
        data.getNet().getPlace().add(pcenter);
        data.getNet().getArc().add(joinarc);
        int count = data.get_specialoperatorscount_value("splitcount-"
                + node.getName());
        for (int i = 1; i <= count; i++) {
            TransitionType trandoub = new TransitionType();
            trandoub.setId(node.getOriginalID().replace("op_1",
                    "op_" + String.valueOf(i + 1)));
            trandoub.setName(test);
            trandoub.getToolspecific().add(trantool);
            ArcType splitarc = new ArcType();
            splitarc.setId("a95" + i);
            splitarc.setSource(pcenter);
            splitarc.setTarget(trandoub);
            splitarc.setInscription(inscription);
            splitarc.setGraphics(new GraphicsArcType());
            splitarc.getToolspecific().add(at);
            data.getNet().getTransition().add(trandoub);
            data.getNet().getArc().add(splitarc);
        }

        data.put_pnmlRefMap(tran.getId(), tran);
        data.getStartNodeMap().put(node, tran);
        data.getEndNodeMap().put(node, tran);
        data.put_originalid_map(BigInteger.valueOf(Long.valueOf(tran.getId())),
                node.getOriginalID());

    }

    public long getIds() {
        return ids;
    }
}
