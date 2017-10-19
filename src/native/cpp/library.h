/**
 * This file contains the frosty library code.
 * 
 * @date 2017
 * @author Mark Lefering
 */
#ifndef TEST_LIBRARY_H
#define TEST_LIBRARY_H

#include <thread>
#include <sstream>
#include <map>
#include <jni.h>
#include <SDL2/SDL.h>

/// Game-pad added callback method name.
#define ADDED_METHOD_NAME "onGamePadAdded"
/// Game-pad added callback method signature.
#define ADDED_METHOD_SIGNATURE "(J)V"

/// Game-pad removed callback method name.
#define REMOVED_METHOD_NAME "onGamePadRemoved"
/// Game-pad removed callback method signature.
#define REMOVED_METHOD_SIGNATURE "(J)V"

/// Game-pad axis motion callback method name.
#define AXIS_MOTION_METHOD_NAME "onGamePadAxisMotion"
/// Game-pad axis motion callback method signature.
#define AXIS_MOTION_METHOD_SIGNATURE "(JBS)V"

/// Game-pad button down callback method name.
#define BUTTON_DOWN_METHOD_NAME "onGamePadButtonDown"
/// Game-pad button down callback method signature.
#define BUTTON_DOWN_METHOD_SIGNATURE "(JB)V"

/// Game-pad button up callback method name.
#define BUTTON_UP_METHOD_NAME "onGamePadButtonUp"
/// Game-pad button up callback method signature.
#define BUTTON_UP_METHOD_SIGNATURE "(JB)V"

/// Game-pad remapped callback method name.
#define REMAPPED_METHOD_NAME "onGamePadRemapped"
/// Game-pad remapped callback method signature.
#define REMAPPED_METHOD_SIGNATURE "(J)V"

/// Game-pad error callback method name.
#define ERROR_METHOD_NAME "onGamePadError"
/// Game-pad error callback method signature.
#define ERROR_METHOD_SIGNATURE "(Ljava/lang/String;)V"

extern "C" {
/**
 * @brief Hooks a GamePadListener to the game-pad manager.
 *
 * This method hooks a GamePadListener to the game-pad manager if the
 * game-pad manager is started and if no game-pad listener is hooked yet.
 *
 * @note This method binds to the
 *       com.copilot.frosty.GamePadManager.hookGamePadListener method.
 *
 * @param jniEnv The JNI environment.
 * @param caller The caller of the method.
 * @param gamePadListener The GamePadListener.
 * @return True if the hook succeed, false if not.
 */
__attribute__ ((used))
JNIEXPORT jboolean
JNICALL Java_com_copilot_frosty_GamePadManager_hookGamePadListener(
    JNIEnv *jniEnv, jobject caller, jobject gamePadListener
);

/**
 * @brief Unhooks the current GamePadListener from the game-pad manager.
 *
 * This method unhooks the current GamePadListener from the game-pad
 * manager if the game-pad manager has a listener hooked.
 *
 * @note This method binds to the
 *       native com.copilot.frosty.GamePadManager.ControllerListener method.
 *
 * @param jniEnv The JNI environment.
 * @param caller The caller of the method.
 * @return True if the unhook succeed, false if not.
 */
__attribute__ ((used))
JNIEXPORT jboolean
JNICALL Java_com_copilot_frosty_GamePadManager_unhookControllerListener(
    JNIEnv *jniEnv, jobject caller
);

/**
 * @brief Sets the game-pad mapping database.
 *
 * This method sets the game-pad mapping database if the database exists. *
 *
 * @note This method binds to the native com.
 *       copilot.frosty.GamePadManager.setGamePadMappingDatabase method.
 *
 * @param jniEnv The JNI environment.
 * @param caller The caller of the method.
 * @param filePath The file path of the game-pad mapping database.
 * @return True if the game-pad mapping database is set successfully, false if not.
 */
__attribute__ ((used))
JNIEXPORT jboolean
JNICALL Java_com_copilot_frosty_GamePadManager_setGamePadMappingDatabase(
    JNIEnv *jniEnv, jobject caller, jstring filePath
);

/**
 * @brief Start game-pad manager method.
 *
 * This is the start game-pad manager method.
 *
 * @note This method binds to the native
 *       com.copilot.frosty.GamePadManager.start method.
 *
 * @param jniEnv The JNI environment.
 * @param caller The caller of the method.
 * @return True if the game-pad manager is started successfully, false if not.
 */
__attribute__ ((used))
JNIEXPORT jboolean
JNICALL Java_com_copilot_frosty_GamePadManager_start(
    JNIEnv *jniEnv, jobject caller
);

/**
 * @brief Stop controller manager method.
 *
 * This method stops the controller manager.
 *
 * @note This method binds to the native
 *       com.copilot.frosty.GamePadManager method.
 * @note This method waits for the main thread to stop.
 *
 * @param jniEnv The JNI environment.
 * @param caller The caller of the method.
 * @return True if the thread is successfully stopped, false if not.
 */
__attribute__ ((used))
JNIEXPORT jboolean
JNICALL Java_com_copilot_frosty_GamePadManager_stop(
    JNIEnv *jniEnv, jobject caller
);
};

/// Java virtual machine pointer.
static JavaVM *_javaVM = nullptr;
/// Game-pad listener.
static jobject _gamePadListener = nullptr;

/// On game-pad added callback method ID.
static jmethodID _onGamePadAddedMethodID;
/// On game-pad removed callback method ID.
static jmethodID _onGamePadRemovedMethodID;
/// On game-pad axis motion callback method ID.
static jmethodID _onGamePadAxisMotionMethodID;
/// On game-pad button down callback method ID.
static jmethodID _onGamePadButtonDownMethodID;
/// On game-pad button up callback method ID.
static jmethodID _onGamePadButtonUpMethodID;
/// On game-pad remapped callback method ID.
static jmethodID _onGamePadRemappedMethodID;
/// On error callback method ID.
static jmethodID _onErrorMethodID;

/// Error message string stream.
static std::stringstream _errorMessageStream;
/// Map from joystick ID to game-pad.
static std::map<SDL_JoystickID, SDL_GameController *> gamePads;

/// SDL thread that captures the SDL events.
static std::thread *_sdlThread = nullptr;
/// SDL thread running state.
static volatile bool _sdlThreadRunning = false;

/**
 * @brief SDL loop.
 *
 * This is the SDL loop which polls for SDL events.
 * If a event occurred a java callback will be called asynchronously.
 */
static void sdlLoop();

/**
 * @brief Calls a void callback asynchronously.
 *
 * This method calls a java void callback asynchronously.
 *
 * @param jmethodID The method id.
 * @param ...
 */
static void callAsyncVoidCallback(jmethodID jmethodID, ...);

#endif