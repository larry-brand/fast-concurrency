/*
 * Copyright (c) 2017, Red Hat Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sample;

import fast.FastLinkedBlockingQueue;
import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

import java.util.concurrent.atomic.AtomicInteger;

import static org.openjdk.jcstress.annotations.Expect.*;

// See jcstress-samples or existing tests for API introduction and testing guidelines

@JCStressTest

// These are the test outcomes.
@Outcome(id = "0", expect = FORBIDDEN, desc = "Bad case peek")
@Outcome(id = "-1", expect = FORBIDDEN, desc = "Bad case pool")
@Outcome(id = "-2", expect = FORBIDDEN, desc = "Bad case peek,pool")
@Outcome(id = "7", expect = ACCEPTABLE, desc = "Good case")
@State
public class ConcurrencyTest {

    final FastLinkedBlockingQueue<Integer> q = new FastLinkedBlockingQueue();
    final AtomicInteger pe = new AtomicInteger();
    final AtomicInteger po = new AtomicInteger();
    volatile boolean inserted = false;
    volatile boolean peeked = false;

    @Actor
    public void actor1() {
        q.add(Integer.valueOf(7));
        inserted = true;
    }

    @Actor
    public void actor2() {
        while (inserted == false) { }

        Integer i = q.peek(); // 50% 7   50% null
        if (i != null) {
            pe.set(i);
        } else {
            pe.set(0);
        }
        peeked = true;
    }

    @Actor
    public void actor3() {
        while (peeked == false) { }

        Integer i = q.poll(); // 50% 7   50% null
        if (i != null) {
            po.set(i);
        } else {
            po.set(0);
        }
        peeked = true;
    }

    @Arbiter
    public void arbiter(I_Result r) {
        if (pe.get() == 7 && po.get() == 7) {
            r.r1 = 7;
        } else if (pe.get() == 7 && po.get() != 7) {
            r.r1 = -1;
        } else if (pe.get() != 7 && po.get() == 7) {
            r.r1 = 0;
        } else {
            r.r1 = -2;
        }
        //r.r1 = pe.get();
    }

}