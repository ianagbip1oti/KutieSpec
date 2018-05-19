package com.github.princesslana.kutispec

import com.github.princesslana.kutiespec.KutieSpec

class SanitySpec : KutieSpec ({
    describe("sanity") {
        it { expect(true) to beTrue }
    }
})