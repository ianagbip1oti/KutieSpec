package com.github.princesslana.kutispec

import com.github.princesslana.kutiespec.KutieSpec

class SanitySpec : KutieSpec ({
    describe("sanity") {
        it("is sane") { expect(true) to beTrue }
    }
})