package com.bryll.hamsv2.config

import com.bryll.hamsv2.repository.IUsersRepository
import com.bryll.hamsv2.repository.IUsersRepositoryImpl
import com.bryll.hamsv2.repository.auth.IAuthRepository
import com.bryll.hamsv2.repository.auth.IAuthRepositoryImpl
import com.bryll.hamsv2.repository.classes.IClassesRepository
import com.bryll.hamsv2.repository.classes.IClassesRepositoryImpl
import com.bryll.hamsv2.repository.enrollment.IEnrollmentRepository
import com.bryll.hamsv2.repository.enrollment.IEnrollmentRepositoryImpl
import com.bryll.hamsv2.repository.student.IStudentRepository
import com.bryll.hamsv2.repository.student.IStudentRepositoryImpl
import com.bryll.hamsv2.repository.subject.ISubjectRepository
import com.bryll.hamsv2.repository.subject.ISubjectRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryProviders {


    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): IAuthRepository{
        return IAuthRepositoryImpl(auth,firestore)
    }


    @Provides
    @Singleton
    fun provideStudentRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage : FirebaseStorage
    ): IStudentRepository{
        return IStudentRepositoryImpl(auth,firestore,storage)
    }

    @Provides
    @Singleton
    fun provideClassesRepository(
        firestore: FirebaseFirestore,
    ): IClassesRepository{
        return IClassesRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideEnrollmentRepository(
        firestore: FirebaseFirestore,
    ): IEnrollmentRepository{
        return IEnrollmentRepositoryImpl(firestore)
    }
    @Provides
    @Singleton
    fun provideSubjectRepository(
        firestore: FirebaseFirestore,
    ): ISubjectRepository{
        return ISubjectRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore,
    ): IUsersRepository{
        return IUsersRepositoryImpl(firestore)
    }

}