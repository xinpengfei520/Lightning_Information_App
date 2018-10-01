/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kymjs.frame.samples.demo4;

import com.kymjs.frame.samples.R;
import com.kymjs.themvp.view.AppDelegate;

/**
 * @author kymjs (http://www.kymjs.com/) on 10/26/15.
 */
public class FragmentDelegate extends AppDelegate {

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_user;
    }
}
