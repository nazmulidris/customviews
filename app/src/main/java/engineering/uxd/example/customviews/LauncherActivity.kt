/*
 * Copyright 2018 Nazmul Idris. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package engineering.uxd.example.customviews

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_launcher.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.singleTop

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        button_custom_drawable.onClick {
            startActivity(intentFor<CustomDrawableActivity>().singleTop())
        }

        button_custom_view.onClick {
            startActivity(intentFor<CustomViewActivity>().singleTop())
        }

        button_custom_layoutmanager.onClick {
            startActivity(intentFor<CustomLayoutManager>().singleTop())
        }

        button_custom_scrolling.onClick {
            startActivity(intentFor<CustomScrolling>().singleTop())
        }

        button_custom_view2.onClick {
            startActivity(intentFor<CustomView2Activity>().singleTop())
        }

        button_custom_viewgroup.onClick {
            startActivity(intentFor<CustomViewGroupActivity>().singleTop())
        }

    }
}
