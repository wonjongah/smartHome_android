package com.example.handol


import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionChecker(val activity: AppCompatActivity,
                        val permissions: Array<String>,
                        val requestCode: Int = 1000) {
    fun check(): Boolean {
        // permissions 배열에 권한이 배열로 존재하면, 예전에 권한획득 안 받은 것만 notGranted에 추려내겠다
        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) !=
                    PackageManager.PERMISSION_GRANTED
        }
        if(notGranted.isEmpty()) { // 권한 획득한 적 있으면 return true
            return true
        }
// 권한 획득한 적 없으면 return false
        ActivityCompat.requestPermissions(
            activity, permissions, requestCode) // 원래는 permissions말고 notGranted
        return false
    }

    fun checkGranted(requestCode: Int,
                     permissions: Array<out String>, // 요청한 permissions 배열
                     grantResults: IntArray) : Boolean { // permissions 갯수만큼 grantResults가 엘리먼트 갯수를 가짐
        if( requestCode == this.requestCode) { // 인덱스 정보까지 주는 filterIndexed
            val notGranted = permissions.filterIndexed { index, s -> // 첫 번째 인자가 인덱스
                grantResults[index]!=PackageManager.PERMISSION_GRANTED // 두 번째 인자가 엘리먼트
            }
            if(notGranted.isEmpty()) {
                return true // 모든 권한 승인 return true
            }
        }
        return false // 하나라도 승인나지 않았으면 return false
    }
}