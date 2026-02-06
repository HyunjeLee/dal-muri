---
trigger: always_on
---

Dalmuri Project AI Rules & Guidelines

1. Code Modification Policy (Copy-Paste First)
Do not modify files directly unless explicitly instructed by the user with a clear command (e.g., "Update the file with this code").
Primary Response: Always provide code in blocks that the user can easily copy and paste.
Validation: After any direct file modification is performed, automatically run build commands (e.g., ./gradlew assembleDebug) to verify project integrity.

2. Project Architecture & Patterns
Package Identity: com.example.dalmuri
Layered Clean Architecture:
data: Implementation of infrastructure. local (Room DB), remote (GPT API), and repository impl.
domain: Pure business logic. model, repository interface, and usecase.
presentation: MVI (Model-View-Intent) pattern.
Structure: FeaturePackage -> Screen, ViewModel, State, Intent.
Global UI components reside in presentation.components.
Android Standards: Strictly follow official Android developer recommendations and modern best practices (Jetpack Compose, Hilt, StateFlow, etc.).

3. Versioning & Dependency Constraints
AGP Constraint: The Android Gradle Plugin (AGP) is strictly pinned to 8.7.3 for ktlint compatibility.
Dependency Management: When suggesting or adding new libraries, ensure they are compatible with AGP 8.7.3. Do not suggest updates that might break this specific version alignment.

4. Debugging & Observability
Developer-Friendly Code: Inject appropriate logging (e.g., Log.d or Timber) and debugging aids in generated code.
Traceability: Ensure MVI state transitions and Intent processing are easy to trace.
Error Handling: Implement robust error handling (e.g., Result patterns) so that the user can easily diagnose issues if a bug or network error occurs.

5. Post-Action Verification
Build Test: After executing any direct code modification, use the terminal to run a build/test task to ensure No-Regression.
Feedback: Report the success or failure of the build immediately after the modification.



6. Language Policy
Response Language: All responses, explanations, and comments must be provided in Korean.
