/**
 * This file contains the yagl4j library code.
 *
 * @date 2017
 * @author Mark Lefering
 */
#include "library.h"

extern "C" {
__attribute__ ((used))
JNIEXPORT jboolean JNICALL Java_com_copilot_yagl4j_GamePadManager_hookGamePadListener(JNIEnv *jniEnv,
                                                                                      jobject caller,
                                                                                      jobject gamePadListener) {
    if (_gamePadListener == nullptr && gamePadListener != nullptr) {
        // Getting pointer to the java virtual machine
        jniEnv->GetJavaVM(&_javaVM);
        // Getting global reference to the gamePadListener
        _gamePadListener = jniEnv->NewGlobalRef(gamePadListener);
        // Getting jclass of the gamePadListener
        jclass tmpListenerClass = jniEnv->GetObjectClass(_gamePadListener);

        // Checking if the jclass is not null
        if (tmpListenerClass != nullptr) {
            // Getting method id of the controller added callback
            _onGamePadAddedMethodID = jniEnv->GetMethodID(tmpListenerClass, ADDED_METHOD_NAME, ADDED_METHOD_SIGNATURE);
            // Getting method id of the controller removed callback
            _onGamePadRemovedMethodID = jniEnv->GetMethodID(tmpListenerClass, REMOVED_METHOD_NAME,
                                                            REMOVED_METHOD_SIGNATURE);
            // Getting method id of the controller axis motion callback
            _onGamePadAxisMotionMethodID = jniEnv->GetMethodID(tmpListenerClass, AXIS_MOTION_METHOD_NAME,
                                                               AXIS_MOTION_METHOD_SIGNATURE);
            // Getting method id of the controller button down callback
            _onGamePadButtonDownMethodID = jniEnv->GetMethodID(tmpListenerClass, BUTTON_DOWN_METHOD_NAME,
                                                               BUTTON_DOWN_METHOD_SIGNATURE);
            // Getting method id of the controller button up callback
            _onGamePadButtonUpMethodID = jniEnv->GetMethodID(tmpListenerClass, BUTTON_UP_METHOD_NAME,
                                                             BUTTON_UP_METHOD_SIGNATURE);
            // Getting method id of the controller remapped callback
            _onGamePadRemappedMethodID = jniEnv->GetMethodID(tmpListenerClass, REMAPPED_METHOD_NAME,
                                                             REMAPPED_METHOD_SIGNATURE);
            // Getting method id of the error callback
            _onErrorMethodID = jniEnv->GetMethodID(tmpListenerClass, ERROR_METHOD_NAME, ERROR_METHOD_SIGNATURE);

            // Hook succeed
            return JNI_TRUE;
        }
    } else if (_gamePadListener != nullptr) {
        // Clearing error message string stream
        _errorMessageStream.clear();
        // Building error message
        _errorMessageStream << "addControllerListener() failed because another GamePadListener is already hooked!";
        // Call error callback
        callAsyncVoidCallback(_onErrorMethodID, jniEnv->NewStringUTF(_errorMessageStream.str().c_str()));
    }

    // Callback add failed
    return JNI_FALSE;
}

__attribute__ ((used))
JNIEXPORT jboolean JNICALL
Java_com_copilot_yagl4j_GamePadManager_unhookGamePadListener(JNIEnv *jniEnv, jobject caller) {
    // Checking if a game-pad listener is hooked
    if (_gamePadListener != nullptr) {
        // Clearing game pad listener
        _gamePadListener = nullptr;

        // Unhook succeed
        return JNI_TRUE;
    }
    // Unhook failed
    return JNI_FALSE;
}

__attribute__ ((used))
JNIEXPORT jboolean JNICALL Java_com_copilot_yagl4j_GamePadManager_setGamePadMappingDatabase(JNIEnv *jniEnv,
                                                                                            jobject caller,
                                                                                            jstring filePath) {
    jboolean rtn = JNI_FALSE;
    // Parsing java string to char*
    const char *filePathCString = jniEnv->GetStringUTFChars(filePath, JNI_FALSE);

    // Add game-pad mappings from file
    if (SDL_GameControllerAddMappingsFromFile(filePathCString) == -1) {
        // Clearing error message string stream
        _errorMessageStream.clear();
        // Building error message
        _errorMessageStream << "setGamePadMappingDatabase(" << filePathCString << ") failed: " << SDL_GetError();
        // Call error callback
        callAsyncVoidCallback(_onErrorMethodID, jniEnv->NewStringUTF(_errorMessageStream.str().c_str()));
    } else {
        // Setting game-pad mappings succeed
        rtn = JNI_TRUE;
    }

    // Release string
    jniEnv->ReleaseStringUTFChars(filePath, filePathCString);
    // Return status
    return rtn;
}

__attribute__ ((used))
JNIEXPORT jboolean JNICALL Java_com_copilot_yagl4j_GamePadManager_start(JNIEnv *jniEnv, jobject caller) {
    // Checking if a thread is already started
    if (_sdlThread == nullptr) {
        // Initializing SDL
        if (SDL_Init(SDL_INIT_EVENTS | SDL_INIT_JOYSTICK | SDL_INIT_GAMECONTROLLER) != 0) {
            // Clearing error message string stream
            _errorMessageStream.clear();
            // Building error message
            _errorMessageStream << "SDL_Init() failed: " << SDL_GetError();
            // Calling error callback
            callAsyncVoidCallback(_onErrorMethodID, jniEnv->NewStringUTF(_errorMessageStream.str().c_str()));
            return JNI_FALSE;
        }

        // Setting _sdlThreadRunning state
        _sdlThreadRunning = true;
        // Create new thread
        _sdlThread = new std::thread(sdlLoop);
        // Start succeed
        return JNI_TRUE;
    }
    // Start failed
    return JNI_FALSE;
}

__attribute__ ((used))
JNIEXPORT jboolean JNICALL Java_com_copilot_yagl4j_GamePadManager_stop(JNIEnv *jniEnv, jobject caller) {
    // Checking if SDL thread exists
    if (_sdlThread != nullptr) {
        // Clearing _sdlThreadRunning state
        _sdlThreadRunning = false;
        // Wait for thread to stop
        _sdlThread->join();

        // Clearing SDL thread
        _sdlThread = nullptr;
        // Quit SDL
        SDL_Quit();

        // Stop succeed
        return JNI_TRUE;
    }
    // Stop failed
    return JNI_FALSE;
}
}

void sdlLoop() {
    SDL_Event tmpEvent;
    // Loop until the SDL thread needs to stop.
    while (_sdlThreadRunning) {
        // Check if there was any data available
        if (SDL_PollEvent(&tmpEvent)) {
            // Switch on event type
            switch (tmpEvent.type) {
                // Game-pad axis motion event
                case SDL_CONTROLLERAXISMOTION:
                    // Call the axis motion callback
                    callAsyncVoidCallback(_onGamePadAxisMotionMethodID, tmpEvent.caxis.which, tmpEvent.caxis.axis,
                                          tmpEvent.caxis.value);
                    break;

                    // Game-pad button down event
                case SDL_CONTROLLERBUTTONDOWN:
                    // Call the button down callback
                    callAsyncVoidCallback(_onGamePadButtonDownMethodID, tmpEvent.cbutton.which,
                                          tmpEvent.cbutton.button);
                    break;

                    // Game-pad button up event
                case SDL_CONTROLLERBUTTONUP:
                    // Call the button up callback
                    callAsyncVoidCallback(_onGamePadButtonUpMethodID, tmpEvent.cbutton.which, tmpEvent.cbutton.button);
                    break;

                    // Game-pad added event
                case SDL_CONTROLLERDEVICEADDED:
                    // Add game-pad to the map of game-pads
                    gamePads[tmpEvent.cdevice.which] = SDL_GameControllerOpen(tmpEvent.cdevice.which);
                    // Call the added callback
                    callAsyncVoidCallback(_onGamePadAddedMethodID, SDL_JoystickInstanceID(
                            SDL_GameControllerGetJoystick(gamePads[tmpEvent.cdevice.which])));
                    break;

                    // Game-pad removed event
                case SDL_CONTROLLERDEVICEREMOVED:
                    // Call the removed callback
                    callAsyncVoidCallback(_onGamePadRemovedMethodID);
                    // Close game-pad
                    SDL_GameControllerClose(gamePads[tmpEvent.cdevice.which]);
                    // Erase game-pad from game-pad map
                    gamePads.erase(tmpEvent.cdevice.which);
                    break;

                    // Game-pad remapped event
                case SDL_CONTROLLERDEVICEREMAPPED:
                    // Call the remapped callback
                    callAsyncVoidCallback(_onGamePadRemappedMethodID, tmpEvent.cdevice.which);
                    break;

                default:
                    break;
            }
        }
        // Sleep for a millisecond
        std::this_thread::sleep_for(std::chrono::milliseconds(1));
    }
}

void callAsyncVoidCallback(jmethodID jmethodID, ...) {
    /// Temp JNI environment.
    JNIEnv *jniEnv;
    /// Variable argument list.
    va_list vaList;

    // Checking if a game-pad listener is hooked
    if (_gamePadListener == nullptr) {
        // Get JNI environment
        switch (_javaVM->GetEnv((void **) &jniEnv, JNI_VERSION_1_8)) {
            // JNI OK
            case JNI_OK:
                break;

                // JNI detached error
            case JNI_EDETACHED:
                // Attach to the current thread
                if (_javaVM->AttachCurrentThread((void **) &jniEnv, NULL) != 0) {
                    // Call error callback
                    callAsyncVoidCallback(_onErrorMethodID,
                                          jniEnv->NewStringUTF("JavaVM->AttachCurrentThread() failed!"));
                    // Nothing left to do
                    return;
                }
                break;

                // JNI version error
            case JNI_EVERSION:
                // Clearing error message string stream
                _errorMessageStream.clear();
                // Building error message
                _errorMessageStream << "JNI version: " << JNI_VERSION_1_8 << " not supported!";

                // Call error callback
                callAsyncVoidCallback(_onErrorMethodID, jniEnv->NewStringUTF(_errorMessageStream.str().c_str()));
                return;

            default:
                return;
        }

        // Start vararg list
        va_start(vaList, jmethodID);
        // Call the void callback
        jniEnv->CallVoidMethodV(_gamePadListener, jmethodID, vaList);
        // Stop vararg list
        va_end(vaList);

        // Check for exceptions
        if (jniEnv->ExceptionCheck())
            // Call error callback
            callAsyncVoidCallback(_onErrorMethodID,
                                  jniEnv->NewStringUTF("Java exception thrown when calling callback"));

        // Detach from current thread
        _javaVM->DetachCurrentThread();
    }
}