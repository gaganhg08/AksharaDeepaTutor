package com.aksharadeepa.tutor.data.repository

import com.aksharadeepa.tutor.data.local.AppDatabase
import com.aksharadeepa.tutor.data.local.UserPreferences
import com.aksharadeepa.tutor.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.*

class TutorRepository(private val db: AppDatabase, private val prefs: UserPreferences) {

    fun getProfileFlow(): Flow<ProfileEntity?> = db.profileDao().getProfile()
    
    suspend fun saveProfile(profile: ProfileEntity) {
        db.profileDao().insertProfile(profile)
    }

    val subjects: Flow<List<SubjectEntity>> = db.subjectDao().getAllSubjects()
    val subSubjects: Flow<List<SubSubjectEntity>> = db.subSubjectDao().getAllSubSubjects()
    fun getAllChaptersFlow() = db.chapterDao().getAllChapters()

    fun getChapters(subjectId: Int) = db.chapterDao().getChaptersBySubject(subjectId)
    
    suspend fun getChapterById(chapterId: Int): ChapterEntity? {
        return db.chapterDao().getAllChapters().firstOrNull()?.find { it.id == chapterId }
    }

    suspend fun toggleChapterCompletion(chapter: ChapterEntity) {
        val newChapter = chapter.copy(
            isCompleted = !chapter.isCompleted,
            completionTimestamp = if (!chapter.isCompleted) System.currentTimeMillis() else null
        )
        db.chapterDao().updateChapter(newChapter)
        
        if (newChapter.isCompleted) incrementChapterGoal()

        // Recalculate SubSubject Progress
        val allChapters = db.chapterDao().getChaptersListBySubject(chapter.subjectId)
        val chaptersInSubSubject = allChapters.filter { it.subSubjectId == chapter.subSubjectId }
        
        val completedInSubSubject = chaptersInSubSubject.count { 
            (it.id == chapter.id && newChapter.isCompleted) || (it.id != chapter.id && it.isCompleted)
        }
        
        val subSubjectProgress = if (chaptersInSubSubject.isNotEmpty()) {
            (completedInSubSubject.toFloat() / chaptersInSubSubject.size * 100).toInt()
        } else 0

        val subSubjectList = db.subSubjectDao().getAllSubSubjects().firstOrNull()
        val targetSubSubject = subSubjectList?.find { it.id == chapter.subSubjectId }
        
        if (targetSubSubject != null) {
            val updatedSubSubject = targetSubSubject.copy(progressPercentage = subSubjectProgress)
            db.subSubjectDao().updateSubSubject(updatedSubSubject)
            
            // Recalculate Subject Progress
            val updatedSubSubjectList = subSubjectList.map { if (it.id == updatedSubSubject.id) updatedSubSubject else it }
            val subSubjectsInSubject = updatedSubSubjectList.filter { it.subjectId == chapter.subjectId }
            
            val subjectProgress = if (subSubjectsInSubject.isNotEmpty()) {
                subSubjectsInSubject.sumOf { it.progressPercentage } / subSubjectsInSubject.size
            } else 0
            
            val subject = db.subjectDao().getSubjectById(chapter.subjectId).firstOrNull()
            if (subject != null) {
                db.subjectDao().updateSubject(subject.copy(progressPercentage = subjectProgress))
            }
        }
    }

    suspend fun getQuizQuestionsForChapter(chapterId: Int, excludeIds: List<Int> = emptyList()): List<QuestionEntity> {
        val questions = db.questionDao().getQuestionsExcluding(chapterId, excludeIds, 5)
        return questions.shuffled() // Extra shuffle to randomize order
    }

    fun getBestScoreForChapter(chapterId: Int): Flow<Int?> = db.quizResultDao().getBestScoreForChapter(chapterId)
    
    fun getAttemptsForChapter(chapterId: Int): Flow<Int> = db.quizResultDao().getAttemptsForChapter(chapterId)
    
    suspend fun saveQuizResult(result: QuizResultEntity) {
        db.quizResultDao().insertResult(result)
        incrementQuizGoal()
        val subject = db.subjectDao().getSubjectById(result.subjectId).firstOrNull()
        if (subject != null) {
            val newProgress = minOf(100, subject.progressPercentage + 2) // Boost score
            db.subjectDao().updateSubject(subject.copy(progressPercentage = newProgress))
        }
    }

    fun getTodayGoalFlow(): Flow<DailyGoalEntity?> {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return db.dailyGoalDao().getGoalByDate(todayStr)
    }

    suspend fun incrementQuizGoal() {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        var goal = db.dailyGoalDao().getGoalByDate(todayStr).firstOrNull()
        if (goal == null) {
            goal = DailyGoalEntity(todayStr, targetQuizzes = prefs.globalTargetQuizzes, targetChapters = prefs.globalTargetChapters)
        }
        goal = goal.copy(completedQuizzes = goal.completedQuizzes + 1)
        db.dailyGoalDao().insertGoal(goal)
        updateStreakLogic(todayStr)
    }

    suspend fun incrementChapterGoal() {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        var goal = db.dailyGoalDao().getGoalByDate(todayStr).firstOrNull()
        if (goal == null) {
            goal = DailyGoalEntity(todayStr, targetQuizzes = prefs.globalTargetQuizzes, targetChapters = prefs.globalTargetChapters)
        }
        goal = goal.copy(chaptersRead = goal.chaptersRead + 1)
        db.dailyGoalDao().insertGoal(goal)
        updateStreakLogic(todayStr)
    }

    private fun updateStreakLogic(todayStr: String) {
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val yesterdayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(yesterday.time)
        
        val lastDate = prefs.lastStudyDate
        if (lastDate != todayStr) {
            if (lastDate == yesterdayStr) {
                prefs.streakCount += 1
            } else if (lastDate.isEmpty()) {
                prefs.streakCount = 1
            } else {
                prefs.streakCount = 1
            }
            prefs.lastStudyDate = todayStr
            
            if (prefs.streakCount > prefs.longestStreak) {
                prefs.longestStreak = prefs.streakCount
            }
        }
    }
    
    val allResults: Flow<List<QuizResultEntity>> = db.quizResultDao().getAllResults()

    suspend fun preloadDummyDataIfNeeded() {
        val existingSubjects = db.subjectDao().getAllSubjects().firstOrNull()
        if (existingSubjects.isNullOrEmpty()) {
            val subjects = listOf(
                SubjectEntity(1, "Science", "Physics, Chemistry, Biology", 0),
                SubjectEntity(2, "Mathematics", "Algebra, Geometry, Trigonometry", 0),
                SubjectEntity(3, "Social Studies", "History, Civics, Geography, Economics", 0)
            )
            db.subjectDao().insertSubjects(subjects)
            
            val subSubjects = listOf(
                // Science Sub-subjects
                SubSubjectEntity(1, 1, "Physics"),
                SubSubjectEntity(2, 1, "Chemistry"),
                SubSubjectEntity(3, 1, "Biology"),
                // Math Sub-subjects
                SubSubjectEntity(4, 2, "Algebra"),
                SubSubjectEntity(5, 2, "Geometry"),
                SubSubjectEntity(6, 2, "Trigonometry"),
                // Social Sub-subjects
                SubSubjectEntity(7, 3, "History"),
                SubSubjectEntity(8, 3, "Civics"),
                SubSubjectEntity(9, 3, "Economics"),
                SubSubjectEntity(10, 3, "Geography")
            )
            db.subSubjectDao().insertSubSubjects(subSubjects)
            
            val chapters = mutableListOf<ChapterEntity>()
            // Science -> Physics
            chapters.add(ChapterEntity(1, 1, 1, "Light Reflection and Refraction"))
            chapters.add(ChapterEntity(2, 1, 1, "Electricity"))
            chapters.add(ChapterEntity(3, 1, 1, "Magnetic Effects of Electric Current"))
            // Science -> Chemistry
            chapters.add(ChapterEntity(4, 1, 2, "Chemical Reactions and Equations"))
            chapters.add(ChapterEntity(5, 1, 2, "Acids, Bases and Salts"))
            chapters.add(ChapterEntity(6, 1, 2, "Metals and Non-metals"))
            // Science -> Biology
            chapters.add(ChapterEntity(7, 1, 3, "Life Processes"))
            chapters.add(ChapterEntity(8, 1, 3, "Heredity and Evolution"))
            chapters.add(ChapterEntity(9, 1, 3, "Our Environment"))

            // Math -> Algebra
            chapters.add(ChapterEntity(10, 2, 4, "Arithmetic Progressions"))
            chapters.add(ChapterEntity(11, 2, 4, "Polynomials"))
            // Math -> Geometry
            chapters.add(ChapterEntity(12, 2, 5, "Triangles"))
            chapters.add(ChapterEntity(13, 2, 5, "Coordinate Geometry"))
            // Math -> Trigonometry
            chapters.add(ChapterEntity(14, 2, 6, "Introduction to Trigonometry"))
            chapters.add(ChapterEntity(15, 2, 6, "Heights and Distances"))

            // Social -> History
            chapters.add(ChapterEntity(16, 3, 7, "Advent of Europeans to India"))
            chapters.add(ChapterEntity(17, 3, 7, "Indian Freedom Struggle"))
            // Social -> Civics
            chapters.add(ChapterEntity(18, 3, 8, "Indian Government"))
            chapters.add(ChapterEntity(19, 3, 8, "Public Administration"))
            // Social -> Economics
            chapters.add(ChapterEntity(20, 3, 9, "Economy and Government"))
            chapters.add(ChapterEntity(21, 3, 9, "Consumer Education"))
            // Social -> Geography
            chapters.add(ChapterEntity(22, 3, 10, "Indian Geography"))
            chapters.add(ChapterEntity(23, 3, 10, "Natural Resources"))
            
            db.chapterDao().insertChapters(chapters)
            
            val questions = mutableListOf<QuestionEntity>()
            var qId = 1
            
            fun addQ(subjId: Int, chapId: Int, q: String, a: String, b: String, c: String, d: String, ans: String, exp: String) {
                questions.add(QuestionEntity(qId++, subjId, chapId, q, a, b, c, d, ans, exp))
            }

            // Chapter 1: Light Reflection and Refraction (15 Questions Total)
            addQ(1, 1, "The laws of reflection hold good for:", "Plane mirror only", "Concave mirror only", "Convex mirror only", "All mirrors irrespective of their shape", "D", "Laws of reflection apply to all reflecting surfaces uniformly.")
            addQ(1, 1, "When an object is placed at the principal focus of a concave mirror, the image is formed at:", "Center of curvature", "Infinity", "Beyond C", "Focus", "B", "Parallel rays after reflection from a concave mirror diverge or converge at infinity when object is at focus.")
            addQ(1, 1, "The power of a lens is -4.0 D. What is its nature?", "Convex lens", "Concave lens", "Plano-convex", "Plano-concave", "B", "Negative power indicates a concave (diverging) lens.")
            addQ(1, 1, "Refractive index of water is:", "1.00", "1.33", "1.52", "2.42", "B", "The absolute refractive index of water is approximately 1.33.")
            addQ(1, 1, "A ray of light traveling from a rarer medium to a denser medium:", "Bends towards the normal", "Bends away from the normal", "Goes undeviated", "Reflects back", "A", "Light slows down in a denser medium, causing it to bend towards the normal.")
            addQ(1, 1, "Which mirror is used by dentists to see enlarged images of teeth?", "Concave mirror", "Convex mirror", "Plane mirror", "Plano-convex mirror", "A", "Concave mirrors form enlarged virtual images when the object is close.")
            addQ(1, 1, "The mirror used as a rear-view mirror in vehicles is:", "Convex mirror", "Concave mirror", "Plane mirror", "Cylindrical mirror", "A", "Convex mirrors provide a wider field of view.")
            addQ(1, 1, "The radius of curvature of a spherical mirror is 20 cm. Its focal length is:", "10 cm", "20 cm", "40 cm", "5 cm", "A", "Focal length is half of the radius of curvature (R/2).")
            addQ(1, 1, "Magnification produced by a plane mirror is:", "-1", "+1", "0", "Infinity", "B", "The image is the same size as the object and virtual (+).")
            addQ(1, 1, "Which of the following materials cannot be used to make a lens?", "Water", "Glass", "Plastic", "Clay", "D", "Clay is opaque and light cannot pass through it.")
            addQ(1, 1, "If the magnification of a lens has a negative value, the image is:", "Virtual and erect", "Real and inverted", "Virtual and inverted", "Real and erect", "B", "Negative magnification always indicates a real, inverted image.")
            addQ(1, 1, "The SI unit of power of a lens is:", "Meter", "Centimeter", "Diopter", "Watt", "C", "Power of a lens is measured in Diopters (D).")
            addQ(1, 1, "Light travels fastest in:", "Glass", "Water", "Vacuum", "Diamond", "C", "The speed of light is maximum in a vacuum (3x10^8 m/s).")
            addQ(1, 1, "When light enters from air to glass, its wavelength:", "Increases", "Decreases", "Remains same", "Becomes zero", "B", "Speed and wavelength decrease, but frequency remains constant.")
            addQ(1, 1, "Snell's Law relates to:", "Reflection", "Refraction", "Dispersion", "Scattering", "B", "It defines the ratio of the sine of the angle of incidence to refraction.")

            // Chapter 2: Electricity
            addQ(1, 2, "The SI unit of electric current is:", "Volt", "Ampere", "Ohm", "Coulomb", "B", "Current is the rate of flow of charge, measured in Amperes.")
            addQ(1, 2, "Which instrument is used to measure potential difference?", "Galvanometer", "Voltmeter", "Ammeter", "Potentiometer", "B", "A voltmeter is connected in parallel to measure potential difference across components.")
            addQ(1, 2, "According to Ohm's Law:", "V = I/R", "V = IR", "V = R/I", "V = I^2R", "B", "Voltage is directly proportional to current, giving V = IR.")
            addQ(1, 2, "Resistors connected in series have:", "Same voltage", "Same current", "Different current", "Zero resistance", "B", "In a series circuit, there is only one path for current to flow.")
            addQ(1, 2, "The commercial unit of electrical energy is:", "Joule", "Watt", "Kilowatt-hour", "Ampere-hour", "C", "1 kWh equals 3.6 million Joules, used for commercial billing.")

            // Chapter 3: Magnetic Effects of Electric Current
            addQ(1, 3, "Magnetic field lines around a straight current-carrying conductor are:", "Straight", "Elliptical", "Concentric circles", "Parabolic", "C", "According to the right-hand thumb rule, field lines form concentric circles.")
            addQ(1, 3, "The core of an electromagnet is made of:", "Soft iron", "Steel", "Copper", "Aluminum", "A", "Soft iron acts as a temporary magnet and loses magnetism when current stops.")
            addQ(1, 3, "Fleming's Left-Hand Rule is used to find the direction of:", "Induced current", "Magnetic field", "Force on a conductor", "Electric current", "C", "It is used in motors to determine the direction of force (motion).")
            addQ(1, 3, "A device that converts electrical energy into mechanical energy is:", "Generator", "Galvanometer", "Motor", "Transformer", "C", "An electric motor uses magnetic force to rotate and perform mechanical work.")
            addQ(1, 3, "The phenomenon of electromagnetic induction was discovered by:", "Oersted", "Faraday", "Maxwell", "Fleming", "B", "Michael Faraday discovered that a changing magnetic field induces current.")

            // Chapter 4: Chemical Reactions and Equations
            addQ(1, 4, "Which of the following is a displacement reaction?", "MgCO3 -> MgO + CO2", "2Na + 2H2O -> 2NaOH + H2", "2H2 + O2 -> 2H2O", "Pb(NO3)2 + 2KI -> PbI2 + 2KNO3", "B", "Sodium displaces hydrogen from water.")
            addQ(1, 4, "Rusting of iron is an example of:", "Reduction", "Oxidation", "Displacement", "Decomposition", "B", "Iron reacts with oxygen and moisture to form iron oxide.")
            addQ(1, 4, "When carbon dioxide is passed through lime water, it turns milky due to formation of:", "Calcium bicarbonate", "Calcium oxide", "Calcium carbonate", "Calcium hydroxide", "C", "Insoluble calcium carbonate precipitates out.")
            addQ(1, 4, "Which of the following gases is used for storage of fresh sample of an oil for a long time?", "Carbon dioxide", "Nitrogen", "Oxygen", "Neon", "B", "Nitrogen prevents the oxidation (rancidity) of oils.")
            addQ(1, 4, "A chemical equation is balanced to satisfy:", "Law of conservation of momentum", "Law of conservation of mass", "Law of conservation of energy", "Boyle's Law", "B", "Matter cannot be created or destroyed in a chemical reaction.")

            // Chapter 5: Acids, Bases and Salts
            addQ(1, 5, "What happens when a base reacts with a metal?", "Salt + Water is formed", "Salt + Hydrogen gas is formed", "No reaction occurs", "Oxygen gas is released", "B", "Strong bases react with certain metals to release hydrogen gas.")
            addQ(1, 5, "The pH of a neutral solution is:", "0", "7", "14", "1", "B", "Pure water and neutral solutions have a pH of exactly 7.")
            addQ(1, 5, "Tooth decay starts when the pH of the mouth is lower than:", "5.5", "7.0", "8.0", "6.5", "A", "Acidic environment demineralizes tooth enamel.")
            addQ(1, 5, "Baking soda is chemically known as:", "Sodium carbonate", "Sodium bicarbonate", "Calcium carbonate", "Calcium chloride", "B", "NaHCO3 is sodium bicarbonate or sodium hydrogen carbonate.")
            addQ(1, 5, "Plaster of Paris is obtained by heating:", "Limestone", "Gypsum", "Washing soda", "Baking powder", "B", "Gypsum (CaSO4·2H2O) is heated to 373K to form Plaster of Paris.")

            // Chapter 6: Metals and Non-metals
            addQ(1, 6, "The only non-metal that is liquid at room temperature is:", "Mercury", "Bromine", "Iodine", "Chlorine", "B", "Bromine is the only liquid non-metal; Mercury is a liquid metal.")
            addQ(1, 6, "Which metal is most malleable?", "Iron", "Aluminum", "Gold", "Copper", "C", "Gold can be beaten into extremely thin sheets.")
            addQ(1, 6, "Ionic compounds are generally:", "Soluble in water", "Insoluble in water", "Gases at room temp", "Poor conductors in molten state", "A", "Ionic compounds dissociate into ions in aqueous solutions.")
            addQ(1, 6, "An alloy of copper and zinc is called:", "Bronze", "Brass", "Solder", "Steel", "B", "Brass is a mixture of Cu and Zn.")
            addQ(1, 6, "Which of the following metals does NOT react with cold or hot water?", "Sodium", "Calcium", "Magnesium", "Iron", "D", "Iron only reacts with steam to form iron oxide and hydrogen.")

            // Chapter 7: Life Processes
            addQ(1, 7, "The breakdown of pyruvate to give carbon dioxide, water and energy takes place in:", "Cytoplasm", "Mitochondria", "Chloroplast", "Nucleus", "B", "Aerobic respiration occurs in the mitochondria.")
            addQ(1, 7, "The kidneys in human beings are a part of the system for:", "Nutrition", "Respiration", "Excretion", "Transportation", "C", "Kidneys filter blood and remove nitrogenous waste.")
            addQ(1, 7, "The xylem in plants is responsible for:", "Transport of water", "Transport of food", "Transport of amino acids", "Transport of oxygen", "A", "Xylem tissues conduct water and minerals from roots to leaves.")
            addQ(1, 7, "Autotrophic mode of nutrition requires:", "Carbon dioxide and water", "Chlorophyll", "Sunlight", "All of the above", "D", "Photosynthesis requires CO2, H2O, chlorophyll, and sunlight.")
            addQ(1, 7, "In amoeba, food is digested in the:", "Food vacuole", "Mitochondria", "Pseudopodia", "Chloroplast", "A", "Amoeba engulfs food creating a vacuole where enzymes digest it.")

            // Chapter 8: Heredity and Evolution
            addQ(1, 8, "The scientist who gave the laws of inheritance is:", "Charles Darwin", "Gregor Mendel", "Lamarck", "Louis Pasteur", "B", "Mendel is known as the father of genetics for his pea plant experiments.")
            addQ(1, 8, "The genotypic ratio of a monohybrid cross is:", "3:1", "1:2:1", "9:3:3:1", "1:1", "B", "The ratio of TT:Tt:tt is 1:2:1.")
            addQ(1, 8, "Which of the following are analogous organs?", "Wings of bird and wings of bat", "Forelimbs of frog and human", "Flippers of whale and human arm", "None", "A", "They have different anatomical structures but perform the same function (flying).")
            addQ(1, 8, "The sex of a human child is determined by:", "X chromosome from mother", "Y chromosome from father", "Both A and B", "The sex chromosomes of the father", "D", "Females pass an X; males pass X or Y, determining the sex.")
            addQ(1, 8, "Evolution by natural selection was proposed by:", "Mendel", "Darwin", "Lamarck", "Wallace", "B", "Charles Darwin authored 'On the Origin of Species'.")

            // Chapter 9: Our Environment
            addQ(1, 9, "Which of the following are primary consumers?", "Carnivores", "Herbivores", "Omnivores", "Decomposers", "B", "Primary consumers eat producers (plants).")
            addQ(1, 9, "Ozone layer is destroyed by:", "SO2", "CO2", "CFCs", "CH4", "C", "Chlorofluorocarbons deplete the stratospheric ozone layer.")
            addQ(1, 9, "The flow of energy in an ecosystem is always:", "Unidirectional", "Bidirectional", "Multidirectional", "Cyclic", "A", "Energy flows from producers up the food chain and is lost as heat.")
            addQ(1, 9, "Which of the following is a biodegradable waste?", "Plastic", "Glass", "Vegetable peels", "Aluminum foil", "C", "Organic matter can be broken down by microorganisms.")
            addQ(1, 9, "In a food chain, the third trophic level is always occupied by:", "Producers", "Herbivores", "Carnivores", "Decomposers", "C", "Carnivores eat herbivores, making them the third level.")

            // Chapter 10: Arithmetic Progressions
            addQ(2, 10, "If the common difference of an AP is 5, then what is a18 - a13?", "5", "20", "25", "30", "C", "a18 - a13 = 5d = 5 * 5 = 25.")
            addQ(2, 10, "The sum of first n odd natural numbers is:", "n^2", "n(n+1)", "n^2 - 1", "n/2", "A", "The sum of 1+3+5...+ (2n-1) equals n squared.")
            addQ(2, 10, "If a, b, c are in AP, then:", "2b = a + c", "b = a + c", "b^2 = ac", "2c = a + b", "A", "The middle term is the arithmetic mean of the other two.")
            addQ(2, 10, "The 10th term of the AP: 5, 8, 11, 14... is:", "32", "35", "38", "185", "A", "an = a + (n-1)d -> 5 + 9(3) = 32.")
            addQ(2, 10, "In an AP, if d = -4, n = 7, an = 4, then a is:", "6", "7", "20", "28", "D", "4 = a + 6(-4) -> 4 = a - 24 -> a = 28.")

            // Chapter 11: Polynomials
            addQ(2, 11, "A quadratic polynomial has at most how many zeroes?", "0", "1", "2", "3", "C", "The degree of a quadratic polynomial is 2, hence at most 2 zeroes.")
            addQ(2, 11, "If α and β are the zeroes of x² - 5x + 6, then α + β is:", "5", "-5", "6", "-6", "A", "Sum of zeroes = -b/a = -(-5)/1 = 5.")
            addQ(2, 11, "The product of zeroes of a cubic polynomial ax³ + bx² + cx + d is:", "-d/a", "c/a", "-b/a", "d/a", "A", "Product of all three zeroes in a cubic polynomial is -d/a.")
            addQ(2, 11, "Which of the following is not a polynomial?", "2x² - 3x + 4", "x + 1/x", "x³ - 1", "5", "B", "Variables in polynomials cannot have negative exponents (1/x = x^-1).")
            addQ(2, 11, "If 1 is a zero of the polynomial p(x) = ax² - 3(a-1)x - 1, then the value of a is:", "1", "-1", "2", "-2", "A", "Substitute x=1: a - 3(a-1) - 1 = 0 -> a - 3a + 3 - 1 = 0 -> -2a = -2 -> a = 1.")

            // Chapter 12: Triangles
            addQ(2, 12, "All equilateral triangles are:", "Congruent", "Similar", "Both", "None", "B", "They have the same shape but can be different sizes.")
            addQ(2, 12, "If two triangles are similar, the ratio of their areas is equal to:", "Ratio of their corresponding sides", "Ratio of the squares of corresponding sides", "Ratio of their perimeters", "Ratio of their altitudes", "B", "Area ratio is the square of the scale factor.")
            addQ(2, 12, "Thales theorem is also known as:", "Pythagoras Theorem", "Basic Proportionality Theorem", "Mid-point Theorem", "Angle Bisector Theorem", "B", "BPT is widely known as Thales Theorem.")
            addQ(2, 12, "In a right angled triangle, the square of hypotenuse is equal to:", "Sum of squares of other two sides", "Difference of squares of other two sides", "Product of other two sides", "None", "A", "This is the statement of Pythagoras Theorem.")
            addQ(2, 12, "Two circles of the same radius are always:", "Similar only", "Congruent", "Neither", "Both", "B", "Same radius implies same size and shape.")

            // Chapter 13: Coordinate Geometry
            addQ(2, 13, "The distance of a point P(x,y) from the origin is:", "x + y", "x² + y²", "√(x² + y²)", "x - y", "C", "Distance formula from (0,0) is √(x² + y²).")
            addQ(2, 13, "The midpoint of a line segment joining A(x1, y1) and B(x2, y2) is:", "((x1-x2)/2, (y1-y2)/2)", "((x1+x2)/2, (y1+y2)/2)", "(x1+x2, y1+y2)", "((x1+y1)/2, (x2+y2)/2)", "B", "Midpoint formula averages the x and y coordinates.")
            addQ(2, 13, "The coordinates of the origin are:", "(1,1)", "(0,1)", "(1,0)", "(0,0)", "D", "Origin is the intersection of the X and Y axes.")
            addQ(2, 13, "The point (-3, 5) lies in which quadrant?", "First", "Second", "Third", "Fourth", "B", "Negative X and positive Y is the second quadrant.")
            addQ(2, 13, "What is the area of a triangle with vertices (0,0), (a,0), and (0,b)?", "ab", "1/2 ab", "a+b", "1/2 (a+b)", "B", "Area = 1/2 * base * height = 1/2 * a * b.")

            // Chapter 14: Introduction to Trigonometry
            addQ(2, 14, "What is the value of sin 30°?", "1/2", "√3/2", "1/√2", "1", "A", "sin 30° is exactly 1/2.")
            addQ(2, 14, "If tan A = 4/3, what is sin A?", "3/5", "4/5", "3/4", "5/4", "B", "Using Pythagoras theorem: hypotenuse = 5. sin A = Opp/Hyp = 4/5.")
            addQ(2, 14, "What is sin²θ + cos²θ equal to?", "0", "1", "-1", "2", "B", "This is the fundamental trigonometric identity.")
            addQ(2, 14, "What is the value of tan 45°?", "0", "1", "√3", "Not defined", "B", "tan 45° = sin 45° / cos 45° = 1.")
            addQ(2, 14, "What is the inverse of cosθ?", "sinθ", "tanθ", "secθ", "cosecθ", "C", "secθ is the reciprocal of cosθ.")

            // Chapter 15: Heights and Distances
            addQ(2, 15, "The angle of elevation of the sun, when the shadow of a pole is equal to its height, is:", "30°", "45°", "60°", "90°", "B", "tanθ = Height / Shadow. If they are equal, tanθ = 1, so θ = 45°.")
            addQ(2, 15, "From a point on the ground, 20m away from the foot of a tower, the angle of elevation is 60°. The height of the tower is:", "20√3 m", "20/√3 m", "10√3 m", "40 m", "A", "tan 60° = h / 20 -> √3 = h / 20 -> h = 20√3.")
            addQ(2, 15, "The line drawn from the eye of an observer to the point in the object viewed is called:", "Horizontal line", "Vertical line", "Line of sight", "Angle of elevation", "C", "The visual path is called the line of sight.")
            addQ(2, 15, "As you move closer to a building, the angle of elevation to its top:", "Decreases", "Increases", "Remains same", "Becomes zero", "B", "The closer you get, the steeper you must look up.")
            addQ(2, 15, "If a kite is flying at a height of 50m and the string makes an angle of 30° with the ground, string length is:", "100 m", "50√3 m", "50 m", "25 m", "A", "sin 30° = 50 / L -> 1/2 = 50 / L -> L = 100.")

            // Chapter 16: Advent of Europeans to India
            addQ(3, 16, "Who discovered the sea route to India in 1498?", "Columbus", "Vasco da Gama", "Magellan", "Captain Cook", "B", "Vasco da Gama reached Calicut in 1498.")
            addQ(3, 16, "The first European country to establish trade with India was:", "England", "France", "Portugal", "Netherlands", "C", "The Portuguese were the first to arrive.")
            addQ(3, 16, "The Battle of Plassey was fought in the year:", "1757", "1764", "1857", "1498", "A", "The British defeated Siraj-ud-Daulah in 1757.")
            addQ(3, 16, "Who was the first Governor General of British India?", "Lord Dalhousie", "Warren Hastings", "Lord Clive", "Lord Cornwallis", "B", "Warren Hastings became the first Governor General in 1773.")
            addQ(3, 16, "The capital of French in India was:", "Goa", "Pondicherry", "Surat", "Machilipatnam", "B", "Pondicherry was the main French settlement.")

            // Chapter 17: Indian Freedom Struggle
            addQ(3, 17, "The First War of Indian Independence occurred in:", "1947", "1857", "1919", "1942", "B", "The Sepoy Mutiny of 1857 is considered the first war of independence.")
            addQ(3, 17, "Who gave the slogan 'Do or Die'?", "Bhagat Singh", "Subhas Chandra Bose", "Mahatma Gandhi", "Bal Gangadhar Tilak", "C", "Gandhi gave this call during the Quit India Movement in 1942.")
            addQ(3, 17, "The Jallianwala Bagh massacre happened in the city of:", "Lahore", "Amritsar", "Delhi", "Mumbai", "B", "It occurred in Amritsar, Punjab in 1919.")
            addQ(3, 17, "Who founded the Indian National Army (INA)?", "Mahatma Gandhi", "Jawaharlal Nehru", "Subhas Chandra Bose", "Sardar Patel", "C", "Subhas Chandra Bose reorganized the INA.")
            addQ(3, 17, "The Dandi March was related to which movement?", "Non-Cooperation", "Civil Disobedience", "Quit India", "Khilafat", "B", "The Salt March initiated the Civil Disobedience Movement.")

            // Chapter 18: Indian Government
            addQ(3, 18, "How many fundamental rights are currently recognized by the Indian Constitution?", "5", "6", "7", "8", "B", "There are 6 fundamental rights (Right to Property was removed).")
            addQ(3, 18, "Who is the executive head of the State in India?", "Chief Minister", "Governor", "President", "Prime Minister", "B", "The Governor is the constitutional head of a State.")
            addQ(3, 18, "The minimum age to become the President of India is:", "25 years", "30 years", "35 years", "40 years", "C", "Article 58 specifies 35 years as the minimum age.")
            addQ(3, 18, "Members of the Rajya Sabha are elected for a term of:", "4 years", "5 years", "6 years", "Life time", "C", "Rajya Sabha members serve a 6-year term.")
            addQ(3, 18, "Which house of parliament can be dissolved?", "Lok Sabha", "Rajya Sabha", "Both", "Neither", "A", "The Lok Sabha can be dissolved, but Rajya Sabha is a permanent body.")

            // Chapter 19: Public Administration
            addQ(3, 19, "UPSC stands for:", "Union Police Service Commission", "Union Public Service Commission", "United Public Service Commission", "Universal Public Service Commission", "B", "UPSC conducts civil services examinations.")
            addQ(3, 19, "Who appoints the Chairman of the UPSC?", "Prime Minister", "President", "Chief Justice of India", "Parliament", "B", "The President appoints the chairman and members.")
            addQ(3, 19, "The backbone of public administration in India is:", "Politicians", "Civil Servants", "Military", "Judiciary", "B", "Civil servants implement government policies on the ground.")
            addQ(3, 19, "Panchayati Raj represents:", "Centralization of power", "Decentralization of power", "Dictatorship", "Monarchy", "B", "It decentralizes administration to the village level.")
            addQ(3, 19, "Which amendment introduced the Panchayati Raj system?", "42nd", "44th", "73rd", "74th", "C", "The 73rd Amendment Act of 1992.")

            // Chapter 20: Economy and Government
            addQ(3, 20, "NITI Aayog replaced which institution?", "Planning Commission", "Finance Commission", "UPSC", "RBI", "A", "NITI Aayog replaced the Planning Commission in 2015.")
            addQ(3, 20, "What is the primary function of the RBI?", "Print currency and control credit", "Collect taxes", "Fund infrastructure", "Provide personal loans", "A", "RBI is the central bank responsible for monetary policy.")
            addQ(3, 20, "A budget is a statement of:", "Past income", "Estimated income and expenditure", "Foreign trade", "Military expenses", "B", "A budget forecasts revenues and expenses for a financial year.")
            addQ(3, 20, "India follows which type of economy?", "Capitalist", "Socialist", "Mixed", "Communist", "C", "India has both public and private sectors working together.")
            addQ(3, 20, "National Income divided by total population gives:", "GDP", "Per Capita Income", "Net National Product", "GNP", "B", "Per Capita Income is the average income earned per person.")

            // Chapter 21: Consumer Education
            addQ(3, 21, "National Consumers Day is celebrated on:", "24th December", "15th March", "2nd October", "26th January", "A", "The Consumer Protection Act received assent on Dec 24, 1986.")
            addQ(3, 21, "ISI mark is a certification mark for:", "Agricultural products", "Industrial products", "Gold jewelry", "Processed food", "B", "ISI mark ensures industrial product standards.")
            addQ(3, 21, "Which act protects consumers in India?", "RTI Act", "Consumer Protection Act", "Companies Act", "FEMA", "B", "The Consumer Protection Act safeguards consumer rights.")
            addQ(3, 21, "AGMARK is specifically used for:", "Electrical goods", "Agricultural commodities", "Clothing", "Vehicles", "B", "AGMARK certifies agricultural products in India.")
            addQ(3, 21, "Filing a complaint in a Consumer Court requires:", "A lawyer", "Huge fees", "No mandatory lawyer or heavy fees", "A police FIR", "C", "Consumer courts are designed to be accessible to common people.")

            // Chapter 22: Indian Geography
            addQ(3, 22, "Which is the highest peak in India?", "Mt. Everest", "K2 (Godwin Austen)", "Kangchenjunga", "Nanda Devi", "C", "Kangchenjunga is the highest peak situated entirely within India.")
            addQ(3, 22, "The Tropic of Cancer passes through how many Indian states?", "5", "6", "7", "8", "D", "It passes through 8 states including Gujarat, MP, and West Bengal.")
            addQ(3, 22, "Which river is known as the 'Sorrow of Bihar'?", "Ganga", "Kosi", "Yamuna", "Brahmaputra", "B", "Kosi causes frequent and devastating floods in Bihar.")
            addQ(3, 22, "The oldest mountain range in India is:", "Himalayas", "Western Ghats", "Aravalli", "Vindhyas", "C", "The Aravalli Range is one of the oldest fold mountains in the world.")
            addQ(3, 22, "India is separated from Sri Lanka by:", "Palk Strait", "10 Degree Channel", "Arabian Sea", "Bay of Bengal", "A", "The Palk Strait connects the Bay of Bengal to the Indian Ocean.")

            // Chapter 23: Natural Resources
            addQ(3, 23, "Which of the following is a renewable resource?", "Coal", "Petroleum", "Solar Energy", "Natural Gas", "C", "Solar energy is inexhaustible and replenished naturally.")
            addQ(3, 23, "Black soil is most suitable for the cultivation of:", "Wheat", "Rice", "Cotton", "Tea", "C", "Black soil (Regur soil) holds moisture well, ideal for cotton.")
            addQ(3, 23, "The Chipko Movement was aimed at protecting:", "Rivers", "Trees", "Wildlife", "Minerals", "B", "Villagers hugged trees to prevent them from being felled.")
            addQ(3, 23, "Bauxite is an ore of which metal?", "Iron", "Aluminum", "Copper", "Zinc", "B", "Bauxite is the primary commercial ore of aluminum.")
            addQ(3, 23, "Which state in India is the largest producer of Mica?", "Jharkhand", "Andhra Pradesh", "Karnataka", "Rajasthan", "B", "Andhra Pradesh leads in mica production in India.")

            db.questionDao().insertQuestions(questions)
        }
    }
}
